package com.example.news.ui.auth.login

data class LoginUserItem(
    val email: String,
    val password: String,
    val isValidEmail: Boolean,
    val isValidPassword: Boolean
){
    companion object {
        fun default(): LoginUserItem {
            return LoginUserItem(
                "kotez.morovez@gmail.com",
                "123123Az!",
                true,
                true
            )
        }
    }
}