package com.example.news.ui.pages

import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import com.example.news.R

class VerifyPage : BasePage() {
    fun waitForPageLoading() {
        val element = Espresso.onView(ViewMatchers.withId(R.id.loginTextView))
        super.waitForPageLoading(element)
    }

    fun checkHeaderText() {
        Espresso.onView(ViewMatchers.withId(R.id.loginTextView)).check(
            ViewAssertions.matches(
                ViewMatchers.withText("Ваш аккаунт не верифицирован, проверьте почту и перейдите по ссылке в письме")
            )
        )
    }
}