package com.app.stronglife.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.app.stronglife.data.remote.ApiService

class UserCodeViewModelFactory(
    private val api: ApiService
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserCodeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserCodeViewModel(api) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}