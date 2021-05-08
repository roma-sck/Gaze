package dev.sasikanth.gaze.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

object DateUtils {
    private val appDateFormat = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.US)

    fun formatToAppDate(date: LocalDate): String = appDateFormat.format(date)
}
