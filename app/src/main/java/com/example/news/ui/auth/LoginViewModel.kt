package com.example.news.ui.auth

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.news.domain.model.auth.LoginUserItem
import com.google.common.base.Strings.isNullOrEmpty
import java.util.regex.Pattern

class LoginViewModel : ViewModel() {
    private var _loginUser = MutableLiveData(LoginUserItem.default())
    val loginUser: LiveData<LoginUserItem>
        get() = _loginUser

    fun setEmail(email: String) {
        val oldUser = _loginUser.value ?: LoginUserItem.default()
        _loginUser.value = oldUser.copy(email = email, isValidEmail = isValidEmail(email))
    }

    fun setPassword(password: String) {
        val oldUser = _loginUser.value ?: LoginUserItem.default()
        _loginUser.value = oldUser.copy(password = password, isValidPassword = isValidPassword(password))
    }

    fun login(email: String, password: String){
        if (_loginUser.value!!.isValidEmail && _loginUser.value!!.isValidPassword){
            val userItem = LoginUserItem(
                email = email,
                password = password,
                isValidEmail = true,
                isValidPassword = true
            )
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
            return !isNullOrEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }
    }
}