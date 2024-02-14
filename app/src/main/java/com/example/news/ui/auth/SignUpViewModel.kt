package com.example.news.ui.auth

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.news.domain.model.auth.SignUpUserItem
import com.google.common.base.Strings
import java.util.regex.Pattern

class SignUpViewModel : ViewModel() {
    private var _signUpUser = MutableLiveData(SignUpUserItem.default())
    val signUpUser: LiveData<SignUpUserItem>
        get() = _signUpUser


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
            val userItem = SignUpUserItem(
                name = name,
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
            return !Strings.isNullOrEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }
    }
}