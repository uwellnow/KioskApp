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
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotificationChannel()
        val notification = createNotification()
        startForeground(NOTIFICATION_ID, notification)

        startPollingLoop()

        return START_STICKY
    }

    private fun startPollingLoop() {
        serviceScope.launch {
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
                                    SystemStatusManager.updateStatus(null)
                                }

                                lastTimestamp = statuses.maxOfOrNull { it.createdAt }
                            }
                        } else {
                            if (retryCount < maxRetries - 1) {
                                delay(2000)
                            }
                        }
                    } else {
                        if (retryCount < maxRetries - 1) {
                            delay(2000)
                        }
                    }
                } catch (e: Exception) {
                    if (retryCount < maxRetries - 1) {
                        delay(2000)
                    }
                }
                retryCount++
            }
        }

        while(coroutineContext.isActive) {
            val currentApiKey = prefsManager.getApiKey()

            if (currentApiKey.isEmpty()) {
                delay(5000)
                continue
            }

            try {
                val startTime = System.currentTimeMillis()
                
                val response = RetrofitClient.pollingApi.pollSystemStatus(
                    apiKey = currentApiKey,
                    statusType = null,
                    since = lastTimestamp,
                    timeout = POLLING_TIMEOUT_SECONDS,
                )

                val elapsedTime = System.currentTimeMillis() - startTime

                if (response.isSuccessful) {
                    val statuses = response.body()

                    if (statuses != null && statuses.isNotEmpty()) {
                        // 변경사항이 있음
                        val machineActive = statuses.firstOrNull { it.isActive && it.statusType == "MACHINE" }
                        val serverActive = statuses.firstOrNull { it.isActive && it.statusType == "SERVER" }
                        val activeStatus = machineActive ?: serverActive
                        
                        if (activeStatus != null) {
                            SystemStatusManager.updateStatus(activeStatus)
                        } else {
                            val hasInactiveStatus = statuses.any { !it.isActive && (it.statusType == "MACHINE" || it.statusType == "SERVER") }
                            SystemStatusManager.updateStatus(null)
                        }

                        // 변경사항이 있으면 lastTimestamp 업데이트
                        lastTimestamp = statuses.maxOfOrNull { it.createdAt }
                        consecutiveTimeouts = 0
                    } else {
                        // 타임아웃 (변경사항 없음) - lastTimestamp는 유지하고 바로 재요청
                        // Long Polling이므로 30초 대기 후 빈 리스트가 온 것이 정상
                        consecutiveTimeouts = 0  // 타임아웃은 정상 동작이므로 리셋
                        // lastTimestamp는 유지 (null로 리셋하지 않음)
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    delay(5000)
                }
            } catch (e: Exception) {
                delay(5000)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
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
