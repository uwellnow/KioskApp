package com.app.stronglife.data.remote

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.app.stronglife.util.SystemStatusManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.coroutines.coroutineContext

class PollingService : Service() {
    private val job = SupervisorJob()
    private val serviceScope = CoroutineScope(Dispatchers.IO + job)
    private lateinit var prefsManager: PrefsManager
    private val POLLING_TIMEOUT_SECONDS = 30

    companion object {
        private const val NOTIFICATION_CHANNEL_ID = "PollingService Channel"
        private const val NOTIFICATION_ID = 1
        private const val TAG = "PollingService"
    }

    override fun onCreate() {
        super.onCreate()
        prefsManager = PrefsManager(applicationContext)
        Log.d(TAG, "PollingService onCreate - PrefsManager 초기화 완료")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "PollingService onStartCommand 호출됨")
        createNotificationChannel()
        val notification = createNotification()
        startForeground(NOTIFICATION_ID, notification)
        Log.d(TAG, "Foreground 서비스 시작됨")

        startPollingLoop()
        Log.d(TAG, "Polling 루프 시작됨")

        return START_STICKY
    }

    private fun startPollingLoop() {
        Log.d(TAG, "startPollingLoop 호출됨")
        serviceScope.launch {
            Log.d(TAG, "pollAllStatuses 코루틴 시작")
            pollAllStatuses()
        }
    }
    
    private suspend fun pollAllStatuses() {
        var lastTimestamp: String? = null
        var consecutiveTimeouts = 0
        val MAX_TIMEOUT_BEFORE_RESET = 1

        // 초기 상태 확인 (since=null로 첫 호출)
        val apiKey = prefsManager.getApiKey()
        if (apiKey.isNotEmpty()) {
            var retryCount = 0
            val maxRetries = 3
            
            while (retryCount < maxRetries && lastTimestamp == null) {
                try {
                    Log.d(TAG, "초기 상태 확인 시작 (since=null) - 시도 ${retryCount + 1}/$maxRetries")
                    val response = RetrofitClient.pollingApi.pollSystemStatus(
                        apiKey = apiKey,
                        statusType = null, // 모든 타입 조회
                        since = null,
                        timeout = 5
                    )
                    if (response.isSuccessful) {
                        val statuses = response.body()
                        if (statuses != null) {
                            Log.d(TAG, "초기 상태 수신: ${statuses.size}개 항목")
                            
                            if (statuses.isEmpty()) {
                                Log.w(TAG, "⚠️ 초기 상태가 빈 리스트입니다. 서버에 상태 데이터가 없을 수 있습니다.")
                                // 빈 리스트여도 계속 시도 (서버에 데이터가 생길 때까지)
                                if (retryCount < maxRetries - 1) {
                                    delay(2000)
                                }
                            } else {
                                val machineActive = statuses.firstOrNull { it.isActive && it.statusType == "MACHINE" }
                                val serverActive = statuses.firstOrNull { it.isActive && it.statusType == "SERVER" }
                                val activeStatus = machineActive ?: serverActive
                                
                                if (activeStatus != null) {
                                    SystemStatusManager.updateStatus(activeStatus)
                                } else {
                                    // 초기 상태에서도 isActive = false인 경우 명시적으로 null 설정
                                    SystemStatusManager.updateStatus(null)
                                }

                                lastTimestamp = statuses.maxOfOrNull { it.createdAt }
                                if (lastTimestamp != null) {
                                    Log.d(TAG, "초기 lastTimestamp 설정 성공: $lastTimestamp")
                                } else {
                                    Log.w(TAG, "초기 상태는 수신했지만 createdAt이 없음")
                                }
                            }
                        } else {
                            Log.w(TAG, "⚠️ 초기 상태 응답 본문이 null입니다. HTTP ${response.code()}")
                            if (retryCount < maxRetries - 1) {
                                delay(2000)
                            }
                        }
                    } else {
                        Log.w(TAG, "초기 상태 확인 실패: ${response.code()}")
                        if (retryCount < maxRetries - 1) {
                            delay(2000)
                        }
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "초기 상태 확인 실패: ${e.message}", e)
                    if (retryCount < maxRetries - 1) {
                        delay(2000)
                    }
                }
                retryCount++
            }
            
            if (lastTimestamp == null) {
                Log.e(TAG, "⚠️ 초기 상태 확인 실패 - lastTimestamp가 null입니다. 첫 호출 후 즉시 응답될 수 있습니다.")
            }
        } else {
            Log.w(TAG, "초기 API 키가 없어 초기 상태 확인 건너뜀")
        }

        while(coroutineContext.isActive) {
            val currentApiKey = prefsManager.getApiKey()

            if (currentApiKey.isEmpty()) {
                Log.d(TAG, "API 키가 없어 5초 대기")
                delay(5000)
                continue
            }

            try {
                val startTime = System.currentTimeMillis()
                Log.d(TAG, "Long polling 요청 시작 - timeout: ${POLLING_TIMEOUT_SECONDS}초, since: $lastTimestamp")
                
                val response = RetrofitClient.pollingApi.pollSystemStatus(
                    apiKey = currentApiKey,
                    statusType = null,
                    since = lastTimestamp,
                    timeout = POLLING_TIMEOUT_SECONDS,
                )

                val elapsedTime = System.currentTimeMillis() - startTime
                Log.d(TAG, "Long polling 응답 수신 - 소요 시간: ${elapsedTime}ms (예상: ${POLLING_TIMEOUT_SECONDS * 1000}ms)")

                if (response.isSuccessful) {
                    val statuses = response.body()

                    if (statuses != null && statuses.isNotEmpty()) {
                        Log.d(TAG, "상태 업데이트 수신: ${statuses.size}개 항목")
                        
                        // isActive = true인 상태 우선 확인
                        val machineActive = statuses.firstOrNull { it.isActive && it.statusType == "MACHINE" }
                        val serverActive = statuses.firstOrNull { it.isActive && it.statusType == "SERVER" }
                        val activeStatus = machineActive ?: serverActive
                        
                        if (activeStatus != null) {
                            // 점검 중 상태
                            SystemStatusManager.updateStatus(activeStatus)
                            Log.d(TAG, "상태 업데이트: ${activeStatus.statusType} - isActive: ${activeStatus.isActive}")
                        } else {
                            // isActive = false인 상태가 있거나, 모든 상태가 false인 경우
                            // 점검 해제 상태로 처리
                            val hasInactiveStatus = statuses.any { !it.isActive && (it.statusType == "MACHINE" || it.statusType == "SERVER") }
                            if (hasInactiveStatus) {
                                Log.d(TAG, "점검 해제 상태 감지 - 모든 상태를 null로 설정")
                            }
                            SystemStatusManager.updateStatus(null)
                        }
                        
                        // 가장 최신 timestamp 사용
                        lastTimestamp = statuses.maxOfOrNull { it.createdAt }
                        consecutiveTimeouts = 0
                    } else {
                        // 빈 응답 = timeout 발생 (서버가 30초 기다렸지만 변경사항 없음)
                        if (elapsedTime < POLLING_TIMEOUT_SECONDS * 1000 - 1000) {
                            Log.w(TAG, "⚠️ Long polling이 제대로 작동하지 않음: 응답 시간이 너무 짧음 (${elapsedTime}ms < ${POLLING_TIMEOUT_SECONDS * 1000}ms)")
                            Log.w(TAG, "⚠️ since 파라미터 확인 필요: $lastTimestamp")
                        } else {
                            Log.d(TAG, "Timeout 발생 (변경사항 없음) - 다음 요청 전송, since 유지: $lastTimestamp")
                        }
                        consecutiveTimeouts++

                        if (consecutiveTimeouts >= MAX_TIMEOUT_BEFORE_RESET) {
                            lastTimestamp = null
                            consecutiveTimeouts = 0
                        }
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e(TAG, "HTTP Error: ${response.code()} ${response.message()}")
                    Log.e(TAG, "Error Body: $errorBody")
                    delay(5000)
                }
            } catch (e: Exception) {
                Log.e(TAG, "네트워크 오류: ${e.message}", e)
                delay(5000)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
        Log.d(TAG, "Polling Service 종료됨")
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun createNotification(): Notification {
        return NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle("키오스크 시스템")
            .setContentText("시스템 상태를 감시 중입니다...")
            .setPriority(NotificationCompat.PRIORITY_MIN)
            .build()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                "Polling Service Channel",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "키오스크 상태 감시 서비스 채널"
            }

            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }
    }

}
