package com.example.news.ui.tests

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.news.ui.pages.HomePage
import com.example.news.ui.pages.LoginPage
import com.example.news.ui.splash.SplashActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class HomeScreenTest {
    @get:Rule
    var activityRule = ActivityScenarioRule(
        SplashActivity::class.java
    )

    private val loginPage = LoginPage()
    private val homePage = HomePage()

    @Test
    fun testHome() {
        with(loginPage){
            waitForPageLoading()
            checkHeaderText()
            tapLogin()
        }

        with(homePage){
            waitForPageLoading()
            checkHeaderText()
            checkToolbarVisibility()
            tapSearch()
            checkSearchVisibility()
            tapClose()
            checkToolbarVisibility()
        }
    }
}
