package com.example.news

import com.example.news.common.ui.StringUtils
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class StringUtilsTest {
    @Test
    fun urlDecodingSuccess() {
        val testString = "https%3A%2F%2Ftest.com%2Ftest"
        val expectation = "https://test.com/test"

        val sut = StringUtils
        val actual = sut.urlDecoding(testString)

        assertEquals("Wrong format", expectation, actual)
    }

    @Test
    fun urlDecodingEmpty() {
        val testString = "%%%%"
        val expectation = "%%%%"

        val sut = StringUtils
        val actual = sut.urlDecoding(testString)

        assertEquals("Wrong format", expectation, actual)
    }
}