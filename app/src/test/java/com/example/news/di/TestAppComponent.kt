package com.example.news.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton


@Component(
    modules = [
        TestAppModule::class
    ]
)
@Singleton
interface TestAppComponent: AppComponent {
    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context
        ): TestAppComponent
    }
}