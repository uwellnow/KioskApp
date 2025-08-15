package com.app.stronglife.data.local.serial

import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.io.IOException

class Gs805SerialDataSource(
    private val cfg: SerialConfig,
    private val listener: SerialListener,
    private val externalScope: CoroutineScope
) {
    private val provider = AndroidSerialPortProvider()
    private var opened: AndroidSerialPortProvider.Opened? = null
    private var readJob: Job? = null
    private val framer = A55AFramer()

    // 송신 직렬화용
    private val sendMutex = Mutex()

    @Synchronized
    fun start(): Boolean {
        if (readJob?.isActive == true) return true
        return try {
            opened = provider.open(cfg).also { _ ->
                // 드라이버가 DTR/RTS를 지원하지 않아 에러가 났으므로 제거
                // try { it.setDTR(true) } catch (_: Throwable) {}
                // try { it.setRTS(true) } catch (_: Throwable) {}
            }

            readJob = externalScope.launch(Dispatchers.IO) {
                val input = requireNotNull(opened).input
                val buf = ByteArray(cfg.readBufferSize)

                try {
                    while (isActive) {
                        val n = input.read(buf) // 블로킹 read
                        if (n <= 0) continue

                        val chunk = buf.copyOfRange(0, n)
                        val frames = framer.feed(chunk)

                        if (frames.isNotEmpty()) {
                            frames.forEach { frame -> listener.onFrame(frame.toHex()) }
                        } else {
                            listener.onRaw(chunk)
                        }
                    }
                } catch (ce: CancellationException) {
                    // 정상 종료
                } catch (io: IOException) {
                    if (isActive) listener.onError(io)
                } catch (t: Throwable) {
                    if (t !is CancellationException) listener.onError(t)
                }
            }
            true
        } catch (t: Throwable) {
            listener.onError(t); false
        }
    }

    @Synchronized
    fun stop() {
        val o = opened
        opened = null
        o?.closeQuietly()       // close 먼저 (블로킹 read 깨우기)
        readJob?.cancel()
        readJob = null
    }

    fun isRunning(): Boolean = readJob?.isActive == true

    // suspend 로 변경: withLock 사용 가능
    suspend fun send(frame: ByteArray) {
        val out = opened?.output ?: run {
            listener.onError(IllegalStateException("Serial not opened")); return
        }
        try {
            sendMutex.withLock {
                out.write(frame)
                out.flush()
                // 필요하면 아주 짧게 쉬기: delay(2)
            }
        } catch (t: Throwable) {
            listener.onError(t)
        }
    }

    // suspend 로 변경
    suspend fun send(cmd: Int, data: ByteArray = byteArrayOf()) {
        send(Gs805Protocol.build(cmd, data))
    }
}
