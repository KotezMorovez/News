package com.example.news.common.ui

object StringUtils {
    private val CHARS = mapOf(
        ' ' to "%20",
        '!' to "%21",
        '\"' to "%22",
        '#' to "%23",
        '$' to "%24",
        '%' to "%25",
        '&' to "%26",
        '\'' to "%27",
        '(' to "%28",
        ')' to "%29",
        '*' to "%2A",
        '+' to "%2B",
        ',' to "%2C",
        '/' to "%2F",
        ':' to "%3A",
        ';' to "%3B",
        '=' to "%3D",
        '?' to "%3F",
        '@' to "%40",
        '[' to "%5B",
        ']' to "%5D"
    )

    private val HEXES = mapOf(
        "%20" to ' ',
        "%21" to '!',
        "%22" to '\"',
        "%23" to '#',
        "%24" to '$',
        "%25" to '%',
        "%26" to '&',
        "%27" to '\'',
        "%28" to '(',
        "%29" to ')',
        "%2A" to '*',
        "%2B" to '+',
        "%2C" to ',',
        "%2F" to '/',
        "%3A" to ':',
        "%3B" to ';',
        "%3D" to '=',
        "%3F" to '?',
        "%40" to '@',
        "%5B" to '[',
        "%5D" to ']'
    )

    fun urlEncoding(url: String): String {
        val newUrl: StringBuilder = StringBuilder()

        for (char in url) {
            if (CHARS.contains(char)) {
                newUrl.append(CHARS[char])
            } else {
                newUrl.append(char)
            }
        }

        return newUrl.toString()
    }

    fun urlDecoding(url: String): String {
        var newUrl: StringBuilder = StringBuilder()
        var toDecode = ""

        var i = 0
        while (i < url.length) {
            if (url[i] == '%' && i < url.length - 2) {
                toDecode = "${url[i]}${url[i + 1]}${url[i + 2]}"
            }

            if (HEXES.contains(toDecode)) {
                newUrl.append(HEXES[toDecode])
                toDecode = ""
                i += 2
            } else {
                newUrl.append(url[i])
            }

            i++
        }

        return newUrl.toString()
    }
}