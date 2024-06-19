package com.example.news.ui.pages

import androidx.annotation.IdRes
import androidx.test.espresso.ViewInteraction
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import com.example.news.ui.utils.waitUntilElementVisible


open class BasePage {
    private val device: UiDevice =
        UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
    val packageName: String = device.currentPackageName

    fun waitForPageLoading(@IdRes element: ViewInteraction?) {
        if (element != null) {
            waitUntilElementVisible(element)
        }
    }
}