package com.example.news.data.repository

import com.example.news.data.mapper.toDomain
import com.example.news.data.service.NewsService
import com.example.news.domain.model.home.request.NewsEverythingRequest
import com.example.news.domain.model.home.response.News
import com.example.news.domain.repository.NewsRepository

class NewsRepositoryImpl : NewsRepository {
    private val newsService: NewsService = NewsService()

    companion object {
        private var instance: NewsRepositoryImpl? = null
        fun getInstance(): NewsRepositoryImpl {
            if (instance == null) {
                instance = NewsRepositoryImpl()
            }
            return instance!!
        }
    }

    override suspend fun getNews(newsEverythingRequest: NewsEverythingRequest): Result<News> {
        return newsService.getEverything(newsEverythingRequest).map {
            it.toDomain()
        }
    }
}