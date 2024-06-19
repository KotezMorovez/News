package com.example.news.ui.pages

import androidx.test.espresso.Espresso
import androidx.test.espresso.matcher.ViewMatchers
import com.example.news.R

class SplashPage: BasePage() {
    fun waitForPageLoading() {
        val element = Espresso.onView(ViewMatchers.withId(R.id.splash))
        super.waitForPageLoading(element)
    }
}