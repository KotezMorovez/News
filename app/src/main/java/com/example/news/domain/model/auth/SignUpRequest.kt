package com.example.news.domain.model.auth

data class SignUpRequest (
    val email: String,
    val password: String,
    val name: String
)