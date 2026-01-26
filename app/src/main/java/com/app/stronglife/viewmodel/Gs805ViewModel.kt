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
        retries: Int = 100,
        timeoutMs: Long = 300L
    ): ByteArray? = withContext(Dispatchers.IO) {

        // 하나의 waiter로 모든 재시도를 관통
        val waiter = CompletableDeferred<ByteArray>()
        pending[cmd]?.cancel()
        pending[cmd] = waiter

        repeat(retries + 1) { attempt ->
            dataSource.send(cmd, data)
            // HW 드라이버 특성상 아주 짧은 틱 정도만
            delay(2)

            val resp = withTimeoutOrNull(timeoutMs) { waiter.await() }
            if (resp != null) {
                pending.remove(cmd)
                return@withContext resp
            }
            // 타임아웃 → 다음 재시도
        }
        val grace = withTimeoutOrNull(300) { waiter.await() }
        pending.remove(cmd)
        if (grace != null) return@withContext grace

        _events.tryEmit(MachineEvent.Offline(cmd))
        null
}

    suspend fun queryErrorCode(retries: Int = 100): SerialResult<Int> {
        val frame = Gs805Protocol.queryErrorCode()
        val dataOnly = frame.copyOfRange(4, frame.size - 1)
        val respBytes = sendAndAwait(0x0C, dataOnly, retries = retries, timeoutMs = 300L)

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
        val respBytes = sendAndAwait(0x15, dataOnly, timeoutMs = 300L)

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
        val respBytes = sendAndAwait(0x01, dataOnly, timeoutMs = 300L)

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
        val b = hexToBytes(hex)
        if (b.size < 5 || b[0] != 0xA5.toByte() || b[1] != 0x5A.toByte()) return
        val cmd = b[3].toInt() and 0xFF

        if (cmd == 0x0C) {
            val code = b[4].toInt() and 0xFF
            // 0x0C 응답은 항상 waiter에 전달 (queryErrorCode가 응답을 받을 수 있도록)
            val waiter = pending.remove(cmd)
            if (waiter != null) {
                waiter.complete(b)
            }
            
            // 추가로 이벤트도 발행 (0x00이 아닌 경우)
            if (code != 0x00) {
                when (code) {
                    0x05 -> _events.tryEmit(MachineEvent.CupDropped(hex))
                    0x10 -> _events.tryEmit(MachineEvent.DrinkCompleted(hex))
                    else -> _events.tryEmit(MachineEvent.ErrorCode(code))
                }
            }
            return
        }

        // 그 외 명령의 응답
        pending.remove(cmd)?.complete(b) ?: run {
            _events.tryEmit(MachineEvent.RawDataReceived(hex)) // 디버깅용 전체 메시지 로깅
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
