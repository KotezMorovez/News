package com.example.news.ui.tests

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.news.R
import com.example.news.ui.pages.HomePage
import com.example.news.ui.pages.LoginPage
import com.example.news.ui.splash.SplashActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@LargeTest
class LoginScreenTest {
    @get:Rule
    var activityRule = ActivityScenarioRule(
        SplashActivity::class.java
    )

    private val loginPage = LoginPage()
    private val homePage = HomePage()

    @Test
    fun testLoginSuccess() {

        with(loginPage) {
            waitForPageLoading()
            checkHeaderText()
            typeText(R.id.emailEditText, "test@test.rr")
            typeText(R.id.passwordEditText, "123123Ax!")
            tapLogin()
        }
        with(homePage) {
            waitForPageLoading()
            checkHeaderText()
        }
    }

    @Test
    fun testLoginEmailFailure() {
        with(loginPage) {
            waitForPageLoading()
            checkHeaderText()
            typeText(R.id.emailEditText, "testtest.rr")
            typeText(R.id.passwordEditText, "123123Ax!")
            tapLogin()
            checkEmailHintVisibility()
        }
    }

    @Test
    fun testLoginPasswordFailure() {
        with(loginPage) {
            waitForPageLoading()
            checkHeaderText()
            typeText(R.id.emailEditText, "test@test.rr")
            typeText(R.id.passwordEditText, "123")
            tapLogin()
            checkPasswordHintVisibility()
        }
    }
}