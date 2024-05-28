package com.example.news.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.news.data.repository.AuthRepositoryImpl
import com.example.news.domain.repository.AuthRepository
import com.example.news.ui.common.SingleLiveEvent
import kotlinx.coroutines.launch
import javax.inject.Inject

class SplashViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {
    private val _checkSuccessEvent: SingleLiveEvent<Unit> = SingleLiveEvent()
    val checkSuccessEvent: LiveData<Unit>
        get() = _checkSuccessEvent

    private val _checkFailureEvent: SingleLiveEvent<Unit> = SingleLiveEvent()
    val checkFailureEvent: LiveData<Unit>
        get() = _checkFailureEvent

    private val _checkVerificationEvent: SingleLiveEvent<Unit> = SingleLiveEvent()
    val checkVerificationEvent: LiveData<Unit>
        get() = _checkVerificationEvent

    fun check() {
        viewModelScope.launch {
            val result = repository.isUserAuthorized()
            val verification = repository.isUserVerified()

            if (result) {
                if (verification) {
                    _checkSuccessEvent.call()
                } else {
                    _checkVerificationEvent.call()
                }
            } else {
                _checkFailureEvent.call()
            }
        }
    }
}