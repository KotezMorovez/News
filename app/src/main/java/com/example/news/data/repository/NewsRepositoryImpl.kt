package com.example.news.data.repository

import com.example.news.data.mapper.toDomain
import com.example.news.data.service.NewsService
import com.example.news.domain.model.home.request.NewsEverythingRequest
import com.example.news.domain.model.home.response.News
import com.example.news.domain.model.profile.SourcesList
import com.example.news.domain.repository.NewsRepository
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val newsService: NewsService
) : NewsRepository {

    override suspend fun getNews(newsEverythingRequest: NewsEverythingRequest): Result<News> {
        return newsService.getEverything(newsEverythingRequest).map {
            it.toDomain()
        }
    }

    override suspend fun getSources(language: String): Result<SourcesList> {
        return newsService.getSources(language).map {
            it.toDomain()
        }
    }
}