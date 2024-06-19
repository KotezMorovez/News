package com.example.news.ui.di

import android.content.Context
import com.example.news.di.AppComponent
import com.example.news.di.TestAppModule
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