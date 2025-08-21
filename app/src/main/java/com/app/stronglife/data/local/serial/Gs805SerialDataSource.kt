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
    @Volatile private var opened: AndroidSerialPortProvider.Opened? = null
    @Volatile private var readJob: Job? = null
    private val framer = A55AFramer()
    private val sendMutex = Mutex()

    @Synchronized
    fun start(restartIfRunning: Boolean = true): Boolean {
        // 1) 이미 돌아가는 세션이 있으면 강제 종료
        if (readJob?.isActive == true) {
            if (!restartIfRunning) return true
            forceStopAndJoin()
        } else {
            // readJob이 죽어도 드라이버 핸들이 살아있을 수 있으니 정리
            forceCloseOpened()
        }

        // 2) 프레이머도 초기화 (잔여 바이트 제거)
        framer.reset()

        return try {
            opened = provider.open(cfg).also {
                // DTR/RTS 미지원: 그대로 두기
                // try { it.setDTR(true) } catch (_: Throwable) {}
                // try { it.setRTS(true) } catch (_: Throwable) {}
            }

            // 3) 읽기 루프 시작
            readJob = externalScope.launch(Dispatchers.IO) {
                val input = requireNotNull(opened).input
                val buf = ByteArray(cfg.readBufferSize)
                try {
                    while (isActive) {
                        val n = input.read(buf) // 블로킹
                        if (n < 0) break            // EOF: 스트림 종료 → 루프 종료
                        if (n == 0) continue        // 타임아웃/스퍼리어스
                        val chunk = buf.copyOfRange(0, n)
                        val frames = framer.feed(chunk)
                        if (frames.isNotEmpty()) {
                            frames.forEach { frame -> listener.onFrame(frame.toHex()) }
                        } else {
                            listener.onRaw(chunk)
                        }
                    }
                } catch (ce: CancellationException) {
                    // 정상 취소
                } catch (io: IOException) {
                    if (isActive) listener.onError(io)
                } catch (t: Throwable) {
                    if (t !is CancellationException) listener.onError(t)
                } finally {
                    // 루프가 끝나면 포트도 닫기 (이중안전)
                    forceCloseOpened()
                }
            }
            true
        } catch (t: Throwable) {
            listener.onError(t)
            false
        }
    }

    @Synchronized
    fun stop() {
        forceStopAndJoin()
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

    // ---- 내부 유틸 ----
    private fun forceCloseOpened() {
        try { opened?.closeQuietly() } catch (_: Throwable) {}
        opened = null
    }

    private fun forceStopAndJoin(timeoutMs: Long = 600L) {
        // 취소 → 포트 닫기 → join(타임아웃)
        val job = readJob
        readJob = null
        job?.cancel()
        forceCloseOpened()
        if (job != null) {
            // UI 스레드 블로킹을 피하려면 여기서 별도 스레드로 돌려도 됨
            try {
                kotlinx.coroutines.runBlocking {
                    withTimeoutOrNull(timeoutMs) {
                        job.join()
                    }
                }
            } catch (_: Throwable) { /* 무시 */ }
        }
    }
}
