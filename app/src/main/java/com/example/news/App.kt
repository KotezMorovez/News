package com.example.news

import android.app.Application
import com.example.news.di.AppComponentHolder

/**
 * Custom app class. Contains basic components initialization.
 */
class App : Application() {

    override fun onCreate() {
        initAppComponent()
        super.onCreate()
    }

    private fun initAppComponent() {
        val component = AppComponentHolder.build(this)
        AppComponentHolder.set(component)
    }
}