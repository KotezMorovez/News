package com.example.news.ui.auth.signup

data class SignUpUserItem(
    val name: String,
    val email: String,
    val password: String,
    val isValidEmail: Boolean,
    val isValidPassword: Boolean,
    val isValidName: Boolean
) {
    companion object {
        fun default(): SignUpUserItem {
            return SignUpUserItem(
                "",
                "",
                "",
                true,
                true,
                true
            )
        }
    }
}