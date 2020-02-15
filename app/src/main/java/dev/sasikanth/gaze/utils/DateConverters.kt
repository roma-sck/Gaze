package dev.sasikanth.gaze.utils

import androidx.room.TypeConverter
import org.threeten.bp.LocalDate

object DateConverters {

  @TypeConverter
  @JvmStatic
  fun toDate(value: String): LocalDate {
    return LocalDate.parse(value)
  }

  @TypeConverter
  @JvmStatic
  fun fromDate(date: LocalDate): String {
    return date.toString()
  }
}
