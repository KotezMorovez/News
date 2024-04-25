package com.example.news.data.service

import com.example.news.data.model.news_api.NewsEntity
import okhttp3.OkHttpClient
import okhttp3.internal.notify
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface NewsApi {
    companion object {
        private var instance: NewsApi? = null
        private val interceptor = HttpLoggingInterceptor()

        fun getInstance(): NewsApi {
            if (instance == null) {
                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

                instance = Retrofit.Builder()
                    .baseUrl("https://newsapi.org/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(OkHttpClient.Builder().addInterceptor(interceptor).build())
                    .build()
                    .create(NewsApi::class.java)
            }
            return instance!!
        }
    }

    @GET("v2/everything")
    fun getEverything(
        @Query("q") query: String? = null,
        @Query("searchIn") searchIn: String? = null,
        @Query("sources") sources: List<String>? = null,
        @Query("domains") domains: String? = null,
        @Query("excludeDomains") excludeDomains: String? = null,
        @Query("from") from: String? = null,
        @Query("to") to: String? = null,
        @Query("language") language: String? = null,
        @Query("sortBy") sortBy:String? = null,
        @Query("pageSize") pageSize:Int? = null,
        @Query("page") page:Int? = null,

        @Header("X-Api-Key") apiKey: String,
    ): Call<NewsEntity>

    @GET("v2/top-headlines")
    fun getHeadlines(
        @Query("country") country: String? = null,
        @Query("category") category: String? = null,
        @Query("sources") sources: List<String>? = null,
        @Query("q") query: String? = null,
        @Query("pageSize") pageSize:Int? = null,
        @Query("page") page:Int? = null,

        @Header("X-Api-Key") apiKey: String,
    ): Call<NewsEntity>
}