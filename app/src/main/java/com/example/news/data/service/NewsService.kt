package com.example.news.data.service

import android.util.Log
import com.example.news.BuildConfig
import com.example.news.data.model.news_api.NewsEntity
import com.example.news.data.model.news_api.SourceEntity
import com.example.news.data.model.news_api.SourcesListEntity
import com.example.news.domain.model.home.request.NewsEverythingRequest
import com.example.news.domain.model.home.request.NewsHeadlineRequest
import retrofit2.awaitResponse

interface NewsServiceInterface {}

class NewsService : NewsServiceInterface {
    private val newsApi: NewsApi = NewsApi.getInstance()

    suspend fun getEverything(newsEverythingRequest: NewsEverythingRequest): Result<NewsEntity> {
        try {
            val response =
                newsApi.getEverything(
                    query = newsEverythingRequest.query,
                    searchIn = newsEverythingRequest.searchIn,
                    sources = newsEverythingRequest.sources?.joinToString(separator = ","),
                    domains = newsEverythingRequest.domains,
                    excludeDomains = newsEverythingRequest.excludeDomains,
                    from = newsEverythingRequest.from,
                    to = newsEverythingRequest.to,
                    language = newsEverythingRequest.language,
                    sortBy = newsEverythingRequest.sortBy,
                    pageSize = newsEverythingRequest.pageSize,
                    page = newsEverythingRequest.page,
                    apiKey = BuildConfig.apiKey
                ).awaitResponse()

            if (!response.isSuccessful) {
                Log.e("News", response.code().toString())
                return Result.failure(IllegalStateException())
            }

            val news = response.body()

            return if (news != null) {
                Result.success(news)
            } else {
                Result.failure(IllegalStateException())
            }
        } catch (t: Throwable) {
            return Result.failure(t)
        }
    }

    suspend fun getHeadlines(newsEverythingRequest: NewsHeadlineRequest): Result<NewsEntity> {
        try {
            val response =
                newsApi.getHeadlines(
                    country = newsEverythingRequest.country,
                    category = newsEverythingRequest.category,
                    sources = newsEverythingRequest.sources?.joinToString(separator = ","),
                    query = newsEverythingRequest.query,
                    pageSize = newsEverythingRequest.pageSize,
                    page = newsEverythingRequest.page,
                    apiKey = BuildConfig.apiKey
                ).awaitResponse()

            if (!response.isSuccessful) {
                return Result.failure(IllegalStateException())
            }

            val news = response.body()

            return if (news != null) {
                Result.success(news)
            } else {
                Result.failure(IllegalStateException())
            }
        } catch (t: Throwable) {
            return Result.failure(t)
        }
    }

    suspend fun getSources(): Result<SourcesListEntity> {
        try {
            val response = newsApi.getSources(
                apiKey = BuildConfig.apiKey
            ).awaitResponse()

            if (!response.isSuccessful) {
                return Result.failure(IllegalStateException())
            }

            val sources = response.body()

            return if (sources != null) {
                Result.success(sources)
            } else {
                Result.failure(IllegalStateException())
            }
        } catch (t: Throwable) {
            return Result.failure(t)
        }
    }
}