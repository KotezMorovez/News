package com.example.news.data.service

import com.example.news.BuildConfig
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class KeyInterceptor : Interceptor {
    private var keyCount = 0

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val updatedRequest: Request = when (keyCount) {
            0 -> {
                keyCount++
                createOriginalRequest(request = request, BuildConfig.apiKey)
            }

            1 -> {
                keyCount++
                createOriginalRequest(request = request, BuildConfig.apiKey2)
            }

            else -> {
                keyCount = 0
                createOriginalRequest(request = request, BuildConfig.apiKey3)
            }
        }

        return chain.proceed(updatedRequest)
    }

    private fun createOriginalRequest(request: Request, key: String): Request {
        return request.newBuilder().header("X-Api-Key", key).build()
    }
}