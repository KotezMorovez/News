package com.example.news.domain.model.auth

data class SignUpUserItem(
    val name: String,
    val email: String,
    val password: String,
    val isValidEmail: Boolean,
    val isValidPassword: Boolean
) {
    companion object {
        fun default(): SignUpUserItem {
            return SignUpUserItem(
                "",
                "",
                "",
                true,
                true
            )
        }
    }
}