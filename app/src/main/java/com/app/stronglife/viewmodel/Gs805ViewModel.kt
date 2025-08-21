package com.app.stronglife.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.stronglife.data.local.serial.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import java.util.concurrent.ConcurrentHashMap

// --- Helper ---
fun ByteArray.toHex(prefix: String = ""): String =
    prefix + joinToString("") { "%02X".format(it) }
fun hexToBytes(hex: String): ByteArray =
    hex.replace(" ", "").chunked(2).map { it.toInt(16).toByte() }.toByteArray()

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

    sealed class MachineEvent {
        data class CupDropped(val hex: String) : MachineEvent()
        data class DrinkCompleted(val hex: String) : MachineEvent()
        data class ErrorCode(val code: Int) : MachineEvent()
        data class Offline(val cmd: Int) : MachineEvent()
        data class RawDataReceived(val hex: String) : MachineEvent()
    }
    private val _events = MutableSharedFlow<MachineEvent>(extraBufferCapacity = 64)
    val events = _events.asSharedFlow()

    private val pending = ConcurrentHashMap<Int, CompletableDeferred<ByteArray>>()

    fun startSerial(): Boolean = dataSource.start()
    fun stopSerial() = dataSource.stop()
    fun isSerialRunning() = dataSource.isRunning()

    // 요청-응답(재전송 포함)
    private suspend fun sendAndAwait(
        cmd: Int,
        data: ByteArray = byteArrayOf(),
        retries: Int = 2,
        timeoutMs: Long = 100L   // 기존 100ms -> 현실적인 기본값으로 상향
    ): ByteArray? = withContext(Dispatchers.IO) {
        repeat(retries + 1) {
            val waiter = CompletableDeferred<ByteArray>()
            pending[cmd]?.cancel()
            pending[cmd] = waiter

            // 데이터소스가 CMD+DATA를 받아 프레임 빌드/전송
            dataSource.send(cmd, data)

            // write 직후 짧은 틱 (일부 칩셋에서 안정적)
            delay(2)

            try {
                return@withContext withTimeout(timeoutMs) { waiter.await() }
            } catch (_: Throwable) {
                // 재시도
            }
        }
        pending.remove(cmd)
        _events.tryEmit(MachineEvent.Offline(cmd))
        null
    }

    suspend fun queryErrorCode(): SerialResult<Int> {
        val frame = Gs805Protocol.queryErrorCode()
        val dataOnly = frame.copyOfRange(4, frame.size - 1)
        val respBytes = sendAndAwait(0x0C, dataOnly, timeoutMs = 1500L)

        val errCode = if (respBytes != null && respBytes.size > 4) {
            respBytes[4].toInt() and 0xFF
        } else { -1 }

        return SerialResult(
            businessResult = errCode,
            sentHex = frame.toHex(),
            responseHex = respBytes?.toHex()
        )
    }

    suspend fun saveRecipe3(drinkNo: Int, slots: List<Pair<Int, Int>>): SerialResult<Boolean> {
        val frame = Gs805Protocol.recipeSeries3(drinkNo, slots)
        val dataOnly = frame.copyOfRange(4, frame.size - 1)
        val respBytes = sendAndAwait(0x15, dataOnly, timeoutMs = 2500L)

        val isSuccess = if (respBytes != null && respBytes.size > 4) {
            (respBytes[4].toInt() and 0x7F) == 0x00
        } else false

        return SerialResult(
            businessResult = isSuccess,
            sentHex = frame.toHex(),
            responseHex = respBytes?.toHex()
        )
    }

    suspend fun makeDrinkNow(drinkNo: Int, localOrCmd: Int = 0x02): SerialResult<Boolean> {
        val frame = Gs805Protocol.makeDrink(drinkNo, localOrCmd)
        val dataOnly = frame.copyOfRange(4, frame.size - 1)
        val respBytes = sendAndAwait(0x01, dataOnly, timeoutMs = 2500L)

        val isSuccess = if (respBytes != null && respBytes.size > 4) {
            (respBytes[4].toInt() and 0x7F) == 0x00
        } else false

        return SerialResult(
            businessResult = isSuccess,
            sentHex = frame.toHex(),
            responseHex = respBytes?.toHex()
        )
    }

    // ===== SerialListener =====
    override fun onFrame(hex: String) {
        _events.tryEmit(MachineEvent.RawDataReceived(hex)) // 디버깅용 전체 메시지 로깅

        val b = hexToBytes(hex)
        if (b.size < 5 || b[0] != 0xA5.toByte() || b[1] != 0x5A.toByte()) return
        val cmd = b[3].toInt() and 0xFF

        val waiter = pending.remove(cmd)
        if (waiter != null) {
            waiter.complete(b)
            return
        }

        // 비동기 이벤트 처리
        if (cmd == 0x0C) {
            val code = b[4].toInt() and 0xFF
            when (code) {
                0x05 -> _events.tryEmit(MachineEvent.CupDropped(hex))
                0x10 -> _events.tryEmit(MachineEvent.DrinkCompleted(hex))
                else -> _events.tryEmit(MachineEvent.ErrorCode(code))
            }
        }
    }

    override fun onRaw(bytes: ByteArray) { /* 필요시 디버그 */ }

    override fun onError(t: Throwable) {
        _events.tryEmit(MachineEvent.ErrorCode(-1))
    }

    override fun onCleared() {
        stopSerial()
        super.onCleared()
    }
}
