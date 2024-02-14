package com.example.news.domain.model.auth

data class LoginUserItem(
    val email: String,
    val password: String,
    val isValidEmail: Boolean,
    val isValidPassword: Boolean
){
    companion object {
        fun default(): LoginUserItem {
            return LoginUserItem(
                "",
                "",
                true,
                true
            )
        }
    }
}