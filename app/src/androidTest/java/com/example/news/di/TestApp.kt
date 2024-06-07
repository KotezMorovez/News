package com.example.news.di

import android.app.Application
import com.example.news.ui.di.DaggerTestAppComponent

class TestApp: Application() {
    override fun onCreate() {
        val component = DaggerTestAppComponent.factory().create(this)
        AppComponentHolder.set(component)

        super.onCreate()
    }
}