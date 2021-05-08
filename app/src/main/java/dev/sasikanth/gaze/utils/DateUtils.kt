package dev.sasikanth.gaze.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import org.apache.commons.lang3.time.DateUtils as ApacheDateUtils

object DateUtils {
    val americanTimeZone: TimeZone = TimeZone.getTimeZone("America/Los_Angeles")
    private val americanDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US).apply {
        timeZone = americanTimeZone
    }
    private val appDateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.US)

    fun formatDate(date: Date): String = americanDateFormat.format(date)

    fun formatToAppDate(date: Date): String = appDateFormat.format(date)

    fun parseDate(date: String): Date = americanDateFormat.parse(date)!!
}

fun Calendar.isAfter(`when`: Calendar): Boolean {
    return ApacheDateUtils.truncatedCompareTo(this, `when`, Calendar.DATE) > 0
}
