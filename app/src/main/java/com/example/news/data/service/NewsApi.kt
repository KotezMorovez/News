package com.example.news.data.service

import com.example.news.data.model.news_api.NewsEntity
import com.example.news.data.model.news_api.SourcesListEntity
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {
    @GET("v2/everything")
    fun getEverything(
        @Query("q") query: String? = null,
        @Query("searchIn") searchIn: String? = null,
        @Query("sources") sources: String? = null,
        @Query("domains") domains: String? = null,
        @Query("excludeDomains") excludeDomains: String? = null,
        @Query("from") from: String? = null,
        @Query("to") to: String? = null,
        @Query("language") language: String? = null,
        @Query("sortBy") sortBy:String? = null,
        @Query("pageSize") pageSize:Int? = null,
        @Query("page") page:Int? = null
    ): Call<NewsEntity>

    @GET("v2/top-headlines")
    fun getHeadlines(
        @Query("country") country: String? = null,
        @Query("category") category: String? = null,
        @Query("sources") sources: String? = null,
        @Query("q") query: String? = null,
        @Query("pageSize") pageSize:Int? = null,
        @Query("page") page:Int? = null
    ): Call<NewsEntity>

    @GET("v2/top-headlines/sources")
    fun getSources(
        @Query("language") language: String? = null
    ): Call<SourcesListEntity>
}