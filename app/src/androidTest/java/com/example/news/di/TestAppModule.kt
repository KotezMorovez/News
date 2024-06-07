package com.example.news.di

import com.example.news.domain.repository.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.Reusable
import kotlinx.coroutines.runBlocking
import org.mockito.Mockito
import org.mockito.Mockito.mock

@Module(includes = [SharedModule::class])
interface TestAppModule {
    companion object {
        @Provides
        @Reusable
        fun bindAuthRepository(): AuthRepository {
            val mock: AuthRepository = mock()
            runBlocking {
                Mockito.`when`(mock.isUserAuthorized()).thenReturn(false)
                Mockito.`when`(mock.isUserVerified()).thenReturn(true)
            }
            return mock
        }
    }
}