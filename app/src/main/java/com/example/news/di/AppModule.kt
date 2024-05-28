package com.example.news.di

import com.example.news.data.repository.AuthRepositoryImpl
import com.example.news.data.repository.NewsRepositoryImpl
import com.example.news.data.repository.ProfileRepositoryImpl
import com.example.news.data.service.AuthService
import com.example.news.data.service.CloudStorageService
import com.example.news.data.service.FirebaseAuthService
import com.example.news.data.service.FirebaseService
import com.example.news.data.service.FirestoreService
import com.example.news.data.service.NewsApi
import com.example.news.data.service.NewsService
import com.example.news.data.service.NewsServiceInterface
import com.example.news.data.service.StorageService
import com.example.news.domain.repository.AuthRepository
import com.example.news.domain.repository.NewsRepository
import com.example.news.domain.repository.ProfileRepository
import dagger.Binds
import dagger.Module
import dagger.Reusable

@Module
interface AppModule {
    @Binds
    @Reusable
    fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Reusable
    fun bindNewsRepository(impl: NewsRepositoryImpl): NewsRepository

    @Binds
    @Reusable
    fun bindProfileRepository(impl: ProfileRepositoryImpl): ProfileRepository

    @Binds
    @Reusable
    fun bindAuthService(impl: FirebaseAuthService): AuthService

    @Binds
    @Reusable
    fun bindNewsService(impl: NewsService): NewsServiceInterface

    @Binds
    @Reusable
    fun bindCloudStorageService(impl: StorageService): CloudStorageService

    @Binds
    @Reusable
    fun bindFirestoreService(impl: FirestoreService): FirebaseService
}