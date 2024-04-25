package com.example.news.ui.auth.login

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.news.data.repository.AuthRepositoryImpl
import com.example.news.domain.model.auth.LoginRequest
import com.example.news.domain.repository.AuthRepository
import com.example.news.ui.common.SingleLiveEvent
import com.google.common.base.Strings.isNullOrEmpty
import kotlinx.coroutines.launch
import java.util.regex.Pattern

class LoginViewModel : ViewModel() {
    private val repository: AuthRepository = AuthRepositoryImpl.getInstance()

    private var _loginUser = MutableLiveData(LoginUserItem.default())
    val loginUser: LiveData<LoginUserItem>
        get() = _loginUser

    private val _errorEvent: SingleLiveEvent<Unit> = SingleLiveEvent()
    val errorEvent: LiveData<Unit>
        get() = _errorEvent

    private val _verificationSuccessEvent: SingleLiveEvent<Unit> = SingleLiveEvent()
    val verificationSuccessEvent: LiveData<Unit>
        get() = _verificationSuccessEvent

    private val _verificationFailureEvent: SingleLiveEvent<Unit> = SingleLiveEvent()
    val verificationFailureEvent: LiveData<Unit>
        get() = _verificationFailureEvent

    fun setEmail(email: String) {
        val oldUser = _loginUser.value ?: LoginUserItem.default()
        _loginUser.value = oldUser.copy(email = email, isValidEmail = isValidEmail(email))
    }

    fun setPassword(password: String) {
        val oldUser = _loginUser.value ?: LoginUserItem.default()
        _loginUser.value =
            oldUser.copy(password = password, isValidPassword = isValidPassword(password))
    }

    fun login(email: String, password: String) {
        val validEmail = isValidEmail(email)
        val validPassword = isValidPassword(password)

        if (validEmail && validPassword) {
            val user = LoginRequest(
                email = email,
                password = password,
            )

            viewModelScope.launch {
                val result = repository.loginUser(user)
                val verification = repository.isUserVerified()

                if (result.isFailure) {
                    val exception = result.exceptionOrNull()
                    if (exception != null) {
                        Log.e("News", exception.stackTraceToString())
                        _errorEvent.call()
                    }
                } else if (result.isSuccess) {
                    if (verification) {
                        _verificationSuccessEvent.call()
                    } else {
                        _verificationFailureEvent.call()
                    }
                }
            }
        }
    }

    companion object {
        private fun isValidPassword(password: String): Boolean {
            val passwordPattern = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,16}$"
            val pattern = Pattern.compile(passwordPattern)
            val matcher = pattern.matcher(password)

            return matcher.matches()
        }

        private fun isValidEmail(email: String): Boolean {
            return !isNullOrEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }
    }
}