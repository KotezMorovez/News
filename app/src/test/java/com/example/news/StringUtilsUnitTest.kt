package com.example.news

import com.example.news.common.ui.StringUtils
import org.junit.Test

import org.junit.Assert.*

class StringUtilsUnitTest {
    @Test
    fun urlDecodingDefault() {
        val testString = "https%3A%2F%2Ftest.com%2Ftest"
        val expectation = "https://test.com/test"

        val sut = StringUtils
        val actual = sut.urlDecoding(testString)

        assertEquals("Wrong format", expectation, actual)
    }

    @Test
    fun urlDecodingAllPercents() {
        val testString = "%%%%"
        val expectation = "%%%%"

        val sut = StringUtils
        val actual = sut.urlDecoding(testString)

        assertEquals("Wrong format", expectation, actual)
    }

    @Test
    fun urlDecodingEmpty() {
        val testString = ""
        val expectation = ""

        val sut = StringUtils
        val actual = sut.urlDecoding(testString)

        assertEquals("Wrong format", expectation, actual)
    }

    @Test
    fun urlEncodingDefault() {
        val testString = "https://test.com/test"
        val expectation = "https%3A%2F%2Ftest.com%2Ftest"

        val sut = StringUtils
        val actual = sut.urlEncoding(testString)

        assertEquals("Wrong format", expectation, actual)
    }

    @Test
    fun urlEncodingNoSymbols() {
        val testString = "AAAA"
        val expectation = "AAAA"

        val sut = StringUtils
        val actual = sut.urlEncoding(testString)

        assertEquals("Wrong format", expectation, actual)
    }

    @Test
    fun urlEncodingEmpty() {
        val testString = ""
        val expectation = ""

        val sut = StringUtils
        val actual = sut.urlDecoding(testString)

        assertEquals("Wrong format", expectation, actual)
    }
}