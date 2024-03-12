package com.example.news.ui.homepage.adapter.item

data class NewsUi(
    val id: String,
    val header: String,
    val body: String,
    val imagesUriList: List<String>,
    val data: String,
    val isFavorite: Boolean
)