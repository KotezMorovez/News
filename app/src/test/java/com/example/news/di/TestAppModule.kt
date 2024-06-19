package com.example.news.di

import com.example.news.domain.repository.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.Reusable
import org.mockito.Mockito.mock

@Module(includes = [SharedModule::class])
interface TestAppModule {
    companion object {
        @Provides
        @Reusable
        fun bindAuthRepository(): AuthRepository {
            return mock()
        }
    }
}