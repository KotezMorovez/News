package com.example.news.ui.utils

import androidx.annotation.IdRes
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.junit.Assert
import java.lang.Thread.sleep

private const val DEFAULT_WAIT_TIME = 10000
private const val DEFAULT_LOAD_TIME = 30000

fun waitUntilElementVisible(element: ViewInteraction, timeOut: Int = DEFAULT_LOAD_TIME) {
    waitUntil(timeOut) { element.check(ViewAssertions.matches(isDisplayed())) }
}

private fun waitUntil(timeOut: Int, @IdRes assertion: () -> Any) {
    val endTime = System.currentTimeMillis() + timeOut
    do {
        runCatching { assertion() }
            .onSuccess { return }
            .onFailure { sleep(100) }
    } while (System.currentTimeMillis() < endTime)
    Assert.fail("Failed to wait for element")
}

fun textVisible(element: ViewInteraction, text: String) = runCatching {
    element.check(ViewAssertions.matches(withText(text)))
}.isSuccess