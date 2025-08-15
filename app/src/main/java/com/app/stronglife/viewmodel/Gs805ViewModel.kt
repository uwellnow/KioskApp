package com.app.stronglife.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.stronglife.data.local.serial.*
import com.app.stronglife.data.model.KioskLogPayload
import com.app.stronglife.data.remote.ApiService
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeoutException


// --- Helper 함수 추가 (ByteArray <-> Hex String) ---
fun ByteArray.toHex(prefix: String = ""): String =
    prefix + joinToString("") { "%02X".format(it) }

fun hexToBytes(hex: String): ByteArray =
    hex.chunked(2).map { it.toInt(16).toByte() }.toByteArray()
// ---------------------------------------------------

/**
 * 시리얼 통신 결과를 담는 데이터 클래스
 * @param T 비즈니스 결과 타입 (ex: Boolean, Int)
 * @param businessResult 실제 함수의 성공/실패 등 결과
 * @param sentHex 전송한 데이터의 HEX 문자열
 * @param responseHex 응답받은 데이터의 HEX 문자열 (타임아웃 시 null)
 */
data class SerialResult<T>(
    val businessResult: T,
    val sentHex: String,
    val responseHex: String?
)


class Gs805ViewModel : ViewModel(), SerialListener {

    private val cfg = SerialConfig(
        devicePath = "/dev/ttyS7",
        baudRate = 9600, dataBits = 8, parity = 0, stopBits = 1
    )
    private val dataSource = Gs805SerialDataSource(cfg, this, viewModelScope)

    // ---- 이벤트 스트림 (EndScreen에서 제조 완료 감지용) ----
    sealed class MachineEvent {
        object CupDropped : MachineEvent()
        object DrinkCompleted : MachineEvent()
        data class ErrorCode(val code: Int) : MachineEvent()
        data class Offline(val cmd: Int) : MachineEvent()
    }
    private val _events = MutableSharedFlow<MachineEvent>(extraBufferCapacity = 64)
    val events = _events.asSharedFlow()

    // ---- 요청 대기 테이블 (CMD별로 1건만 대기 가정) ----
    private val pending = ConcurrentHashMap<Int, CompletableDeferred<ByteArray>>()

    fun startSerial(): Boolean = dataSource.start()
    fun stopSerial() = dataSource.stop()
    fun isSerialRunning() = dataSource.isRunning()

    /**
     * [변경] 내부: 재전송 포함 CMD 응답을 기다리는 함수.
     * 성공 시 응답 ByteArray, 타임아웃 시 null을 반환.
     */
    private suspend fun sendAndAwait(
        cmd: Int,
        data: ByteArray = byteArrayOf(),
        retries: Int = 2,
        timeoutMs: Long = 100L
    ): ByteArray? = withContext(Dispatchers.IO) { // 반환 타입 ByteArray?로 변경
        repeat(retries + 1) {
            val waiter = CompletableDeferred<ByteArray>()
            pending[cmd]?.cancel()
            pending[cmd] = waiter

            // 실제 전송은 dataSource가 프레임을 만들어 전송한다고 가정
            dataSource.send(cmd, data)

            try {
                return@withContext withTimeout(timeoutMs) { waiter.await() }
            } catch (t: Throwable) {
                // 타임아웃 시 아무것도 안하고 다음 루프로 재시도
            }
        }
        // 모든 재시도 실패 시
        pending.remove(cmd)
        _events.tryEmit(MachineEvent.Offline(cmd))
        return@withContext null // 예외 대신 null 반환
    }

    // ===== 비즈니스용 래퍼 (반환타입 SerialResult로 변경) =====

    suspend fun queryErrorCode(): SerialResult<Int> {
        val frame = Gs805Protocol.queryErrorCode() // 전송 프레임 생성
        val dataOnly = frame.copyOfRange(4, frame.size - 1)
        val respBytes = sendAndAwait(0x0C, dataOnly)

        val errCode = if (respBytes != null && respBytes.size > 4) {
            respBytes[4].toInt() and 0xFF
        } else {
            -1 // 응답 없거나 실패 시 임의의 에러코드
        }
        return SerialResult(
            businessResult = errCode,
            sentHex = frame.toHex(),
            responseHex = respBytes?.toHex()
        )
    }

    suspend fun saveRecipe3(drinkNo: Int, slots: List<Pair<Int, Int>>): SerialResult<Boolean> {
        val frame = Gs805Protocol.recipeSeries3(drinkNo, slots)
        val dataOnly = frame.copyOfRange(4, frame.size - 1)
        val respBytes = sendAndAwait(0x15, dataOnly)

        val isSuccess = if (respBytes != null && respBytes.size > 4) {
            (respBytes[4].toInt() and 0x7F) == 0x00
        } else {
            false
        }
        return SerialResult(
            businessResult = isSuccess,
            sentHex = frame.toHex(),
            responseHex = respBytes?.toHex()
        )
    }

    suspend fun makeDrinkNow(drinkNo: Int, localOrCmd: Int = 0x02): SerialResult<Boolean> {
        val frame = Gs805Protocol.makeDrink(drinkNo, localOrCmd)
        val dataOnly = frame.copyOfRange(4, frame.size - 1)
        val respBytes = sendAndAwait(0x01, dataOnly)

        val isSuccess = if (respBytes != null && respBytes.size > 4) {
            (respBytes[4].toInt() and 0x7F) == 0x00
        } else {
            false
        }
        return SerialResult(
            businessResult = isSuccess,
            sentHex = frame.toHex(),
            responseHex = respBytes?.toHex()
        )
    }

    // ===== SerialListener =====
    override fun onFrame(hex: String) {
        // 공통: 펜딩 해소 & 이벤트 분배
        val b = hexToBytes(hex)
        if (b.size < 5 || b[0] != 0xA5.toByte() || b[1] != 0x5A.toByte()) return
        val cmd = b[3].toInt() and 0xFF

        // 1) 대기중인 요청 완료
        pending.remove(cmd)?.complete(b)

        // 2) 0x0C 비동기 이벤트 해석
        if (cmd == 0x0C) {
            val code = b[4].toInt() and 0xFF
            when (code) {
                0x05 -> _events.tryEmit(MachineEvent.CupDropped)     // A5 5A 03 0C 05 13
                0x10 -> _events.tryEmit(MachineEvent.DrinkCompleted) // A5 5A 03 0C 10 1E
                else -> _events.tryEmit(MachineEvent.ErrorCode(code))
            }
        }
    }

    override fun onRaw(bytes: ByteArray) { /* 디버그 필요시 사용 */ }

    override fun onError(t: Throwable) {
        _events.tryEmit(MachineEvent.ErrorCode(-1)) // 임의(-1)로 신호
    }

    override fun onCleared() {
        stopSerial()
        super.onCleared()
    }
}
