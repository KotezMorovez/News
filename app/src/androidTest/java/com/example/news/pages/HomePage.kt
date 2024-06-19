package com.example.news.ui.pages

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import com.example.news.R

class HomePage: BasePage() {

    fun waitForPageLoading() {
        val element = Espresso.onView(ViewMatchers.withId(R.id.homePageToolbarTitle))
        super.waitForPageLoading(element)
    }

    fun checkHeaderText() {
        Espresso.onView(ViewMatchers.withId(R.id.homePageToolbarTitle)).check(
            ViewAssertions.matches(
                ViewMatchers.withText("Главная")
            )
        )
    }

    fun checkSearchVisibility() {
        Espresso.onView(ViewMatchers.withId(R.id.homePageSearchEditText)).check(
            ViewAssertions.matches(
                ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)
            )
        )
    }

    fun checkToolbarVisibility() {
        Espresso.onView(ViewMatchers.withId(R.id.mainContainer)).check(
            ViewAssertions.matches(
                ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)
            )
        )
    }

    fun tapSearch() {
        Espresso.onView(ViewMatchers.withId(R.id.homePageToolbarSearch)).perform(ViewActions.click())
    }

    fun tapClose() {
        Espresso.onView(ViewMatchers.withId(R.id.homePageToolbarSearchClose)).perform(ViewActions.click())
    }
}