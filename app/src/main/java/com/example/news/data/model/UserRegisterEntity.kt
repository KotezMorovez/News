package com.example.news.data.model

class UserRegisterEntity (
    val name: String,
    val email: String,
    val password: String,
    val language: String,
    val sources: List<String>
) {
}