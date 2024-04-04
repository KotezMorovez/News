package com.example.news.data.model.news_api

data class RequestEntity(
    val query: String? = null,
    val searchIn: String? = null,
    val sources: List<String>? = null,
    val domains: String? = null,
    val excludeDomains: String? = null,
    val from: String? = null,
    val to: String? = null,
    val language: String? = null,
    val sortBy:String? = null,
    val pageSize:Int? = null,
    val page:Int? = null,
    val country: String? = null,
    val category: String? = null,
)