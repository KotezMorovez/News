package com.example.news.domain.repository

import com.example.news.domain.model.home_page.request.NewsEverythingRequest
import com.example.news.domain.model.home_page.response.News

interface NewsRepository {
    suspend fun getNews(newsEverythingRequest: NewsEverythingRequest): Result<News>
}