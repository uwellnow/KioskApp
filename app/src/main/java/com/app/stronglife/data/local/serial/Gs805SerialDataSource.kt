package com.app.stronglife.data.local.serial

import kotlinx.coroutines.*
import java.io.IOException

/**
 * 순수 통신 전담: 코루틴 IO에서 상시 수신, 패킷 프레이밍, 원본 Hex 전달.
 * 비즈니스 판단/파싱은 ViewModel 담당.
 */
class Gs805SerialDataSource(
    private val cfg: SerialConfig,
    private val listener: SerialListener,
    private val externalScope: CoroutineScope
) {

    private val provider = AndroidSerialPortProvider()
    private var opened: AndroidSerialPortProvider.Opened? = null
    private var readJob: Job? = null
    private val framer = A55AFramer()

    @Synchronized
    fun start(): Boolean {
        if (readJob?.isActive == true) return true
        return try {
            opened = provider.open(cfg)
            readJob = externalScope.launch(Dispatchers.IO) {
                val input = requireNotNull(opened).input
                val buf = ByteArray(cfg.readBufferSize)
                try {
                    while (isActive) {
                        val n = input.read(buf)
                        if (n <= 0) throw IOException("Serial read EOF / $n")
                        val chunk = buf.copyOfRange(0, n)
                        val frames = framer.feed(chunk)
                        if (frames.isNotEmpty()) {
                            frames.forEach { listener.onFrame(it.toHex()) }
                        } else {
                            listener.onRaw(chunk)
                        }
                    }
                } catch (t: Throwable) {
                    listener.onError(t)
                }
            }
            true
        } catch (t: Throwable) {
            listener.onError(t)   // 서버 로깅 등으로 흘러가게
            false                 // ← 앱 안 죽고 ‘미연결’ 상태로 진행
        }
    }

    @Synchronized
    fun stop() {
        readJob?.cancel()
        readJob = null
        opened?.closeQuietly()
        opened = null
    }

    fun isRunning(): Boolean = readJob?.isActive == true

    /** 이미 완성된 바이트 프레임 전송 (예: Gs805Protocol.build 결과) */
    fun send(frame: ByteArray) {
        val out = opened?.output ?: error("Serial not opened")
        runCatching {
            out.write(frame); out.flush()
        }.onFailure { listener.onError(it) }
    }

    /** CMD + DATA로 빌드해서 전송 */
    fun send(cmd: Int, data: ByteArray = byteArrayOf()) = send(Gs805Protocol.build(cmd, data))
}
