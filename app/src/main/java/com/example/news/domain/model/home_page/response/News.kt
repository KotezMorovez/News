package com.example.news.domain.model.home_page.response

data class News (
    val totalResults: Int,
    val articles: List<Article>
)