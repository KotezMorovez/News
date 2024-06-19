package com.example.news.ui.auth.reset_password

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.news.domain.repository.AuthRepository
import com.example.news.ui.common.SingleLiveEvent
import com.google.common.base.Strings
import kotlinx.coroutines.launch
import javax.inject.Inject

class ForgetPasswordViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _checkSuccessEvent: SingleLiveEvent<Unit> = SingleLiveEvent()

    val successEvent: LiveData<Unit>
        get() = _checkSuccessEvent

    private val _checkFailureEvent: SingleLiveEvent<ForgetPasswordError> = SingleLiveEvent()

    val failureEvent: LiveData<ForgetPasswordError>
        get() = _checkFailureEvent

    fun sendEmail(email: String) {
        viewModelScope.launch {
            val validation: Boolean = isValidEmail(email)

            if (!validation) {
                _checkFailureEvent.value = ForgetPasswordError.VALIDATION_ERROR
            } else {
                val result = authRepository.resetPassword(email)

                if (result.isSuccess) {
                    _checkSuccessEvent.call()
                } else {
                    _checkFailureEvent.value = ForgetPasswordError.SERVICE_ERROR
                }
            }
        }
    }

    companion object {
        private fun isValidEmail(email: String): Boolean {
            return !Strings.isNullOrEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }
    }
}