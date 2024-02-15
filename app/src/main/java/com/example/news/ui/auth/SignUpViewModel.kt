package com.example.news.ui.auth

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.news.data.auth.AuthRepositoryImpl
import com.example.news.domain.model.auth.SignUpRequest
import com.example.news.ui.common.SingleLiveEvent
import com.google.common.base.Strings
import kotlinx.coroutines.launch
import java.util.regex.Pattern

class SignUpViewModel : ViewModel() {
    private val repository = AuthRepositoryImpl.getInstance()

    private var _signUpUser = MutableLiveData(SignUpUserItem.default())
    val signUpUser: LiveData<SignUpUserItem>
        get() = _signUpUser

    private val _errorEvent: SingleLiveEvent<Unit> = SingleLiveEvent()
    val errorEvent: LiveData<Unit>
        get() = _errorEvent

    private val _signUpSuccessEvent: SingleLiveEvent<Unit> = SingleLiveEvent()

    val signUpSuccessEvent: LiveData<Unit>
        get() = _signUpSuccessEvent

    fun setName(name: String){
        val oldUser = _signUpUser.value ?: SignUpUserItem.default()
        _signUpUser.value = oldUser.copy(name = name)
    }

    fun setEmail(email: String) {
        val oldUser = _signUpUser.value ?: SignUpUserItem.default()
        _signUpUser.value = oldUser.copy(email = email, isValidEmail = isValidEmail(email))
    }

    fun setPassword(password: String) {
        val oldUser = _signUpUser.value ?: SignUpUserItem.default()
        _signUpUser.value = oldUser.copy(password = password, isValidPassword = isValidPassword(password))
    }

    fun signUp(name: String, email: String, password: String){
        if (_signUpUser.value!!.isValidEmail && _signUpUser.value!!.isValidPassword){
            val user = SignUpRequest(
                name = name,
                email = email,
                password = password
            )

            viewModelScope.launch {
                val result = repository.registerUser(user)

                if (result.isFailure) {
                    val exception = result.exceptionOrNull()
                    if (exception != null) {
                        Log.e("News", exception.stackTraceToString())
                        _errorEvent.call()
                    }
                } else if (result.isSuccess) {
                    _signUpSuccessEvent.call()
                }
            }
        }
    }

    companion object{
        private fun isValidPassword(password: String): Boolean {
            val passwordPattern = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,16}$"
            val pattern = Pattern.compile(passwordPattern)
            val matcher = pattern.matcher(password)

            return matcher.matches()
        }

        private fun isValidEmail(email: String): Boolean{
            return !Strings.isNullOrEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }
    }
}