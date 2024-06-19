package com.example.news.ui.pages


import androidx.annotation.IdRes
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.example.news.R

class LoginPage : BasePage() {
    fun waitForPageLoading() {
        val element = Espresso.onView(withId(R.id.loginTextView))
        super.waitForPageLoading(element)
    }

    fun checkHeaderText() {
        Espresso.onView(withId(R.id.loginTextView)).check(matches(withText("Добро пожаловать")))
    }

    fun tapLogin() {
        Espresso.onView(withId(R.id.loginButton)).perform(click())
    }

    fun typeText(@IdRes id: Int, text: String) {
        Espresso.onView(withId(id)).perform(replaceText(text))
    }

    fun checkEmailHintVisibility() {
        Espresso
            .onView(withText("Неправильный адрес почты"))
            .check(matches(ViewMatchers.isDisplayed()))
    }

    fun checkPasswordHintVisibility() {
        Espresso
            .onView(
                withText(
                    "Неверный формат пароля.\nПароль должен содержать 8-16 символов, минимум одну заглавную букву и минимум один из символов @#$%^&+=!"
                )
            )
            .check(matches(ViewMatchers.isDisplayed()))
    }
}