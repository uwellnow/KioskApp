package com.app.stronglife.util

import com.app.stronglife.data.model.SystemStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object SystemStatusManager {
    private val _statusFlow = MutableStateFlow<SystemStatus?>(null)
    val statusFlow = _statusFlow.asStateFlow()

    /**
     *   새로운 상태 받으면 PollingService에서 StateFlow에 Emit
     */
    fun updateStatus(newStatus: SystemStatus?) {
        _statusFlow.value = newStatus
    }
}