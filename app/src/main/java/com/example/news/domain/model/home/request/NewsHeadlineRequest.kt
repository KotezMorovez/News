package com.example.news.domain.model.home.request

data class NewsHeadlineRequest (
    val country: String? = null,
    val category: String? = null,
    val sources: List<String>? = null,
    val query: String? = null,
    val pageSize : Int = 10,
    val page: Int = 1
)
