package com.example.news.domain.model.home_page.request

data class NewsEverythingRequest(
    val query: String? = null,
    val searchIn: String? = null,
    val sources: List<String>? = null,
    val domains: String? = null,
    val excludeDomains: String? = null,
    val from: String? = null,
    val to: String? = null,
    val language: String? = null,
    val sortBy:String? = null,
    val pageSize:Int = 10,
    val page:Int = 1
)