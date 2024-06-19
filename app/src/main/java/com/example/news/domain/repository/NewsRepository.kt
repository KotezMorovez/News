package com.example.news.domain.repository

import com.example.news.domain.model.home.request.NewsEverythingRequest
import com.example.news.domain.model.home.response.News
import com.example.news.domain.model.profile.SourcesList

interface NewsRepository {
    suspend fun getNews(newsEverythingRequest: NewsEverythingRequest): Result<News>
    suspend fun getSources(language: String): Result<SourcesList>
}