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

    /** 내부: 재전송 포함해서 CMD 응답을 기다리는 서스펜드 함수 */
    private suspend fun sendAndAwait(
        cmd: Int,
        data: ByteArray = byteArrayOf(),
        retries: Int = 2,           // 총 3회(초발 + 2회 재전송)
        timeoutMs: Long = 100L
    ): ByteArray = withContext(Dispatchers.IO) {
        var last: Throwable? = null
        repeat(retries + 1) { attempt ->
            val waiter = CompletableDeferred<ByteArray>()
            pending[cmd]?.cancel() // 기존 대기자 정리
            pending[cmd] = waiter

            // 전송
            dataSource.send(cmd, data)

            try {
                return@withContext withTimeout(timeoutMs) { waiter.await() }
            } catch (t: Throwable) {
                last = t
                // 다음 루프에서 재전송
            }
        }
        pending.remove(cmd)
        _events.tryEmit(MachineEvent.Offline(cmd))
        throw last ?: TimeoutException("No response for cmd=0x${cmd.toString(16)}")
    }

    // ===== 비즈니스용 래퍼 =====

    suspend fun queryErrorCode(): Int {
        val resp = sendAndAwait(0x0C) // 데이터 없음(AA55 02 0C 0D는 빌더가 전송해줌)
        // resp: A5 5A 03 0C ErrorCode SUM
        val bytes = hexToBytes(resp.toHex("")) // resp가 ByteArray긴 한데 onFrame에서 hex만 올려서 통일
        return bytes[4].toInt() and 0xFF
    }

    suspend fun saveRecipe3(drinkNo: Int, slots: List<Pair<Int, Int>>): Boolean {
        // 전체 프레임을 만들고, CMD/DATA만 꺼내서 전송(API는 send(cmd,data) 형태)
        val frame = Gs805Protocol.recipeSeries3(drinkNo, slots)
        val dataOnly = frame.copyOfRange(4, frame.size - 1) // [Drink_NO .. 마지막 데이터]
        val resp = sendAndAwait(0x15, dataOnly) // A5 5A 03 0x15 STA SUM
        val sta = resp[4].toInt() and 0x7F
        return sta == 0x00
    }

    suspend fun makeDrinkNow(drinkNo: Int, localOrCmd: Int = 0x02): Boolean {
        val frame = Gs805Protocol.makeDrink(drinkNo, localOrCmd)
        val resp = sendAndAwait(0x01, frame.copyOfRange(4, frame.size - 1))
        // resp: A5 5A 03 0x01 STA SUM
        val bytes = hexToBytes(resp.toHex(""))
        val sta = bytes[4].toInt() and 0x7F
        return sta == 0x00
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
