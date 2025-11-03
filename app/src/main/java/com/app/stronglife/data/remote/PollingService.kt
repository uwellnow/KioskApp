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

class PollingService : Service() {
    private val job = SupervisorJob()
    private val serviceScope = CoroutineScope(Dispatchers.IO + job)
    private lateinit var prefsManager: PrefsManager
    private val STATUE_TYPE = null // MACHINE, SERVER
    private val POLLING_TIMEOUT_SECONDS = 60

    companion object {
        private const val NOTIFICATION_CHANNEL_ID = "PollingServiceChannel"
        private const val NOTIFICATION_ID = 1
        private const val TAG = "PollingService"
    }

    override fun onCreate() {
        super.onCreate()
        prefsManager = PrefsManager(applicationContext)
        Log.d(TAG, "PrefsManager 초기화 완료")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotificationChannel()
        val notification = createNotification()
        startForeground(NOTIFICATION_ID, notification)

        Log.d(TAG, "Polling Service 시작됨")


        return super.onStartCommand(intent, flags, startId)
    }

    private fun startPollingLoop() {
        serviceScope.launch {
            var lastTimestamp: String? = null

            while(isActive) {
                val apiKey = prefsManager.getApiKey()

                if (apiKey.isEmpty()) {
                    Log.w(TAG, "API Key가 설정되지 않았습니다. 5초 후 재시도합니다.")
                    delay(5000)
                    continue
                }

                try {
                    Log.d(TAG, "Polling 요청... (since: $lastTimestamp)")

                    val response = RetrofitClient.pollingApi.pollSystemStatus(
                        apiKey = apiKey,
                        statusType = STATUE_TYPE,
                        since = lastTimestamp,
                        timeout = POLLING_TIMEOUT_SECONDS,
                    )

                    if (response.isSuccessful) {
                        val status = response.body()

                        if (status != null) {
                            Log.i(TAG, "!!! 상태 변화 감지: ${status.isActive}")
                            SystemStatusManager.updateStatus(status)
                            lastTimestamp = status.createdAt
                        } else {
                            Log.d(TAG, "타임아웃, 즉시 재요청합니다.")
                        }
                    } else {
                        Log.e(TAG, "HTTP Error : ${response.code()} ${response.message()}")
                        delay(5000)
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "네트워크 또는 기타 오류: ${e.message}")
                    delay(5000)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel() // 코루틴 같이 종료
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