package com.example.news.ui.verification

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.news.domain.repository.AuthRepository
import com.example.news.ui.common.SingleLiveEvent
import kotlinx.coroutines.launch
import javax.inject.Inject

class VerificationViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {
    private val _errorEvent: SingleLiveEvent<Unit> = SingleLiveEvent()
    val errorEvent: LiveData<Unit>
        get() = _errorEvent

    private val _successEvent: SingleLiveEvent<Unit> = SingleLiveEvent()
    val successEvent: LiveData<Unit>
        get() = _successEvent

    private val _logoutSuccessEvent: SingleLiveEvent<Unit> = SingleLiveEvent()
    val logoutSuccessEvent: LiveData<Unit>
        get() = _logoutSuccessEvent

    fun sendEmail() {
        viewModelScope.launch {
            val result = repository.sendVerificationEmail()
            if (result.isSuccess) {
                _successEvent.call()
            } else {
                _errorEvent.call()
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            val result = repository.logoutUser()
            if (result.isSuccess) {
                _logoutSuccessEvent.call()
            } else {
                _errorEvent.call()
            }
        }
    }
}