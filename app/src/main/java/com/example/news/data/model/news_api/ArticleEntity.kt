package com.example.news.data.model.news_api

data class ArticleEntity (
    val author: String?,
    val title: String,
    val description: String?,
    val url: String,
    val urlToImage: String?,
    val publishedAt: String,
    val content: String
)