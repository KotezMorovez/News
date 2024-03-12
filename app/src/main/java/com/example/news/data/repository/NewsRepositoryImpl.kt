package com.example.news.data.repository

import com.example.news.domain.repository.NewsRepository

class NewsRepositoryImpl : NewsRepository {

    companion object {
        private var instance: NewsRepositoryImpl? = null
        fun getInstance() : NewsRepositoryImpl {
            if (instance == null){
                instance = NewsRepositoryImpl()
            }
            return instance!!
        }
    }


}