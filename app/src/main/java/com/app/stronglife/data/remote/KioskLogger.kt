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
    private val machineId: Long,              // ★ 필수
    private val storeName: String? = null
) {
    // === 내부 큐 아이템: Payload or Flush 배리어 ===
    private sealed interface QueueItem {
        data class Payload(val payload: KioskLogPayload) : QueueItem
        data class Flush(val ack: CompletableDeferred<Unit>) : QueueItem
    }

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val queue = Channel<QueueItem>(capacity = Channel.BUFFERED)

    // 단말 식별 해시(16비트) + 시간(36비트) + 카운터(12비트)
    private val deviceHash16 = (apiKey.hashCode() and 0xFFFF).toLong()
    private val counter = AtomicInteger(0)

    init {
        // 소비 루프: 순서를 보장하기 위해 "재시도는 여기서만" 처리(큐로 되돌려 넣지 않음)
        scope.launch {
            for (item in queue) {
                when (item) {
                    is QueueItem.Payload -> postWithRetry(item.payload)
                    is QueueItem.Flush -> item.ack.complete(Unit) // 앞선 payload가 모두 처리된 시점
                }
            }
        }
        // 화면 스코프가 끝나면 같이 정리하고 싶을 경우 유지
        // externalScope.coroutineContext[Job]?.invokeOnCompletion { scope.cancel() }
    }

    private fun nowIso(): String =
        DateTimeFormatter.ISO_INSTANT.format(Instant.now())

    private fun newUniqueId(): Long {
        val t36 = System.currentTimeMillis() and ((1L shl 36) - 1)
        val c12 = (counter.getAndIncrement() and 0xFFF).toLong()
        return (deviceHash16 shl 48) or (t36 shl 12) or c12
    }

    // 서버 전송(지수 백오프, 무손실 재시도; 순서 보장)
    private suspend fun postWithRetry(payload: KioskLogPayload) {
        var backoffMs = 1_000L
        while (true) {
            try {
                val r: Response<ResponseBody> = service.postKioskLog(apiKey, payload)
                if (!r.isSuccessful) error("HTTP ${r.code()}")
                return // 성공 시 종료
            } catch (_: Throwable) {
                delay(backoffMs)
                backoffMs = min(backoffMs * 2, 60_000L)
                // 큐로 재삽입하지 않음(순서/flush 보장)
            }
        }
    }

    /**
     * 이벤트 로그 적재 (비동기)
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
            errorDetail = detail,
            commandSent = commandHex,
            response = responseHex
        )
        // queue.trySend(QueueItem.Payload(payload))
        val item = QueueItem.Payload(payload)
        if (!queue.trySend(item).isSuccess) {
            scope.launch { queue.send(item) }   // ★ 드랍 방지: 현재 스레드 블로킹 없이 보장 enqueue
        }
    }

    /**
     * flush(): 현재까지 큐에 들어간 모든 로그가 서버 전송될 때까지 대기.
     * @param timeoutMs 최댓 대기 시간(네트워크 지연 보호)
     */
    suspend fun flush(timeoutMs: Long = 500L) {
        if (!scope.isActive) return

        val ack = CompletableDeferred<Unit>()
        val barrier = QueueItem.Flush(ack)

        // 1) 배리어를 반드시 큐에 싣는다 (trySend 실패 시 send로 보강)
        if (!queue.trySend(barrier).isSuccess) {
            // 내부 scope 컨텍스트에서 보장 enqueue
            withContext(scope.coroutineContext) { queue.send(barrier) }
        }

        // 2) 앞선 Payload들이 모두 postWithRetry를 통과하면 소비 루프가 complete 해줌
        withTimeout(timeoutMs) { ack.await() }
    }

    /**
     * (선택) 종료: 큐를 닫고 내부 작업 종료를 기다림
     */
    suspend fun closeAndJoin(timeoutMs: Long = 1_000L) {
        queue.close()
        withTimeout(timeoutMs) {
            scope.coroutineContext[Job]?.join()
        }
    }
}
