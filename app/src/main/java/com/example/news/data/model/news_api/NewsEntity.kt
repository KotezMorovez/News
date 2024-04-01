package com.example.news.data.model.news_api

data class NewsEntity (
    val status: String,
    val totalResults: Int,
    val articles: List<ArticleEntity>
)