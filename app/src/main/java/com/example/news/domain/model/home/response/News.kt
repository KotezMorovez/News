package com.example.news.domain.model.home.response

data class News (
    val totalResults: Int,
    val articles: List<Article>
)