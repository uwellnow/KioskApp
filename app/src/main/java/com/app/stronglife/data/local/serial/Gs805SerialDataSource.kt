package com.app.stronglife.data.local.serial

import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.io.IOException

class Gs805SerialDataSource(
    private val cfg: SerialConfig,
    private val listener: SerialListener,
) {
    private val provider = AndroidSerialPortProvider()
    private var opened: AndroidSerialPortProvider.Opened? = null
    private var readJob: Job? = null
    private var scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val framer = A55AFramer()
    private val sendMutex = Mutex()

    @Synchronized
    fun start(): Boolean {
        if (readJob?.isActive == true) return true
        return try {
            // 드라이버가 지원하면 여기서 readTimeout 설정(예: cfg.readTimeoutMs)
            opened = provider.open(cfg)
            framer.reset() // 세션 시작 시 프레이머 초기화 권장

            readJob = scope.launch {
                val localOpened = checkNotNull(opened) { "Serial not opened" }
                val input = localOpened.input
                val buf = ByteArray(cfg.readBufferSize)

                try {
                    while (isActive) {
                        // opened가 중간에 닫혔으면 탈출
                        if (opened !== localOpened) break

                        val n = try {
                            input.read(buf) // 블로킹 (timeout 있으면 InterruptedIOException 처리)
                        } catch (io: IOException) {
                            // close로 인해 깬 경우 정상 종료로 간주
                            if (opened !== localOpened || !isActive) break
                            throw io // 진짜 I/O 오류
                        }

                        if (n <= 0) continue
                        val chunk = buf.copyOfRange(0, n)
                        val frames = framer.feed(chunk)
                        if (frames.isNotEmpty()) frames.forEach { listener.onFrame(it.toHex()) }
                        else listener.onRaw(chunk)
                    }
                } catch (_: CancellationException) {
                    // no-op
                } catch (t: Throwable) {
                    // 세션 중 실제 오류만 보고
                    if (isActive) listener.onError(t)
                }
            }
            true
        } catch (t: Throwable) {
            listener.onError(t); false
        }
    }

    @Synchronized
    fun stop() {
        // 1) 루프 종료 보장
        runCatching { runBlocking { readJob?.cancelAndJoin() } }
        readJob = null

        // 2) 스트림/FD 닫기 (read 깨우기)
        val o = opened
        opened = null
        runCatching { o?.closeQuietly() }

        // 3) 프레이머 리셋 (세션 간 잔여 프레임 제거)
        framer.reset()
    }

    fun isRunning(): Boolean = readJob?.isActive == true

    suspend fun send(frame: ByteArray) {
        val out = opened?.output ?: run {
            listener.onError(IllegalStateException("Serial not opened")); return
        }
        try {
            sendMutex.withLock {
                out.write(frame)
                out.flush()
            }
        } catch (t: Throwable) {
            listener.onError(t)
        }
    }

    suspend fun send(cmd: Int, data: ByteArray = byteArrayOf()) {
        send(Gs805Protocol.build(cmd, data))
    }
}
