package com.example.news.ui.common

import android.annotation.SuppressLint
import android.content.Context
import com.example.news.R
import org.joda.time.DateTime
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

@SuppressLint("SimpleDateFormat")
class DateUtils @Inject constructor(private val context: Context) {
    private val dateFormatter = SimpleDateFormat("dd MMM", Locale.forLanguageTag("ru"))
    private val timeFormatter = SimpleDateFormat("HH:mm", Locale.forLanguageTag("ru"))

    fun getElapsedTime(fromDate: DateTime): String {
        val now = DateTime()
        val todayMidnight = DateTime().withTimeAtStartOfDay().millis
        val firstDayOfCurrentYear = DateTime().withDayOfYear(1).millis

        val yesterdayMidnight = todayMidnight - MILLIS_IN_DAY
        val publishedAt = fromDate.millis
        val period = now.millis - fromDate.millis

        val date: String = dateFormatter
            .format(fromDate.toDate())
            .replace(".", "")

        val time: String = timeFormatter
            .format(fromDate.toDate())

        if (period < MILLIS_IN_MINUTE) {
            return context.resources.getString(R.string.date_less_minute_ago)
        }

        if (period < MILLIS_IN_HOUR) {
            return context.resources.getString(R.string.date_minutes_ago, period / MILLIS_IN_MINUTE)
        }

        if (period < MILLIS_IN_THREE_HOUR) {
            return context.resources.getString(R.string.date_hours_ago, period / MILLIS_IN_HOUR)
        }

        if (publishedAt >= todayMidnight) {
            return context.resources.getString(R.string.date_today_at, time)
        }

        if (publishedAt >= yesterdayMidnight) {
            return context.resources.getString(R.string.date_yesterday_at, time)
        }

        if (publishedAt >= firstDayOfCurrentYear){
            return context.resources.getString(R.string.date_less_year_ago, date, time)
        }

        return context.resources.getString(R.string.date_years_ago, time)
    }

    companion object {
        private const val MILLIS_IN_MINUTE = 60 * 1000L
        private const val MILLIS_IN_HOUR = 60 * 60 * 1000L
        private const val MILLIS_IN_THREE_HOUR = 3 * 60 * 60 * 1000L
        private const val MILLIS_IN_DAY = 24 * 60 * 60 * 1000L
    }
}