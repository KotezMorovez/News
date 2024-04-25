package com.example.news.domain.repository

import com.example.news.domain.model.home.request.NewsEverythingRequest
import com.example.news.domain.model.home.response.News

interface NewsRepository {
    suspend fun getNews(newsEverythingRequest: NewsEverythingRequest): Result<News>
}