package com.example.news.domain.model.auth

import java.net.IDN

data class Profile(
    val id: String,
    val email: String,
    val name: String,
    val imageUrl: String?
)