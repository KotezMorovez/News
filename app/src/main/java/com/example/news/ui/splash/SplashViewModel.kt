package com.example.news.ui.splash

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.news.data.repository.AuthRepositoryImpl
import com.example.news.domain.repository.AuthRepository
import com.example.news.ui.common.SingleLiveEvent
import kotlinx.coroutines.launch

class SplashViewModel : ViewModel() {
    private val repository: AuthRepository = AuthRepositoryImpl.getInstance()
    private val _checkSuccessEvent: SingleLiveEvent<Unit> = SingleLiveEvent()

    val checkSuccessEvent: LiveData<Unit>
        get() = _checkSuccessEvent

    private val _checkFailureEvent: SingleLiveEvent<Unit> = SingleLiveEvent()

    val checkFailureEvent: LiveData<Unit>
        get() = _checkFailureEvent

    fun check() {
        viewModelScope.launch {

            val result = repository.isUserAuthorized()

            if (result) {
                _checkSuccessEvent.call()
            } else {
                _checkFailureEvent.call()
            }
        }
    }
}