// app/src/main/java/com/app/stronglife/data/remote/KioskLogger.kt
package com.app.stronglife.data.remote

import com.app.stronglife.data.model.KioskLogPayload
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import okhttp3.ResponseBody
import retrofit2.Response
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.min

class KioskLogger(
    private val apiKey: String,
    private val service: ApiService,
    externalScope: CoroutineScope,
    private val machineId: Long,              // ★ 필수로 변경 (null 금지)
    private val storeName: String? = null
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val queue = Channel<KioskLogPayload>(capacity = Channel.BUFFERED)

    // 단말 식별 해시(16비트) + 시간(36비트) + 카운터(12비트) = 64비트 고유 ID
    private val deviceHash16 = (apiKey.hashCode() and 0xFFFF).toLong()
    private val counter = AtomicInteger(0)

    init {
        scope.launch {
            var backoffMs = 1_000L
            for (payload in queue) {
                try {
                    val r: Response<ResponseBody> = service.postKioskLog(apiKey, payload)
                    if (!r.isSuccessful) error("HTTP ${r.code()}")
                    backoffMs = 1_000L
                } catch (_: Throwable) {
                    delay(backoffMs)
                    backoffMs = min(backoffMs * 2, 60_000L)
                    queue.send(payload) // 재시도
                }
            }
        }
        externalScope.coroutineContext[Job]?.invokeOnCompletion { scope.cancel() }
    }

    private fun nowIso(): String =
        DateTimeFormatter.ISO_INSTANT.format(Instant.now())

    private fun newUniqueId(): Long {
        val t36 = System.currentTimeMillis() and ((1L shl 36) - 1)
        val c12 = (counter.getAndIncrement() and 0xFFF).toLong()
        return (deviceHash16 shl 48) or (t36 shl 12) or c12
    }

    /**
     * [개선] 모든 로그를 처리하는 단일 함수
     * @param detail 로그의 상세 설명 (예: "QueryErrorCode")
     * @param isError 성공(FRAME)인지 실패(ERROR)인지 여부
     * @param commandHex 전송한 명령어
     * @param responseHex 수신한 응답
     */
    fun logEvent(
        detail: String,
        isError: Boolean,
        commandHex: String? = null,
        responseHex: String? = null
    ) {
        val payload = KioskLogPayload(
            errorId = newUniqueId(),
            timestamp = nowIso(),
            machineId = machineId,
            storeName = storeName,
            errorType = if (isError) "ERROR" else "FRAME",
            errorDetail = detail, // 상세 설명을 errorDetail 필드에 저장
            commandSent = commandHex,
            response = responseHex // 응답을 response 필드에 저장
        )
        queue.trySend(payload)
    }
}
