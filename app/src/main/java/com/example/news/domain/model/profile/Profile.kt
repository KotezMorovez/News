package com.example.news.domain.model.profile

data class Profile(
    val id: String,
    val email: String,
    val name: String,
    val imageUrl: String?,
    val language: String,
    val sources: List<String>
)