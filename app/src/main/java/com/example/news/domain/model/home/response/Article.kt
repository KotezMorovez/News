package com.example.news.domain.model.home.response

import org.joda.time.DateTime


data class Article(
    val id: String,
    val author: String?,
    val title: String?,
    val description: String?,
    val url: String,
    val imageUrl: String?,
    val publishedAt: DateTime,
    val content: String
)
