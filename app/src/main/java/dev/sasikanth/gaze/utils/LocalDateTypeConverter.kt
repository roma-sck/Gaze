package dev.sasikanth.gaze.utils

import androidx.room.TypeConverter
import java.time.LocalDate
import java.util.Date

object LocalDateTypeConverter {

    @JvmStatic
    @TypeConverter
    fun toLocalDate(value: String): LocalDate {
        return LocalDate.parse(value)
    }

    @JvmStatic
    @TypeConverter
    fun fromLocalDate(date: LocalDate): String {
        return date.toString()
    }
}
