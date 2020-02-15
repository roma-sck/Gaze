package dev.sasikanth.gaze.utils

import androidx.room.TypeConverter
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import org.threeten.bp.LocalDate

object DateConverters {

  @TypeConverter
  @JvmStatic
  @FromJson
  fun toDate(value: String): LocalDate {
    return LocalDate.parse(value)
  }

  @TypeConverter
  @JvmStatic
  @ToJson
  fun fromDate(date: LocalDate): String {
    return date.toString()
  }
}
