package dev.sasikanth.gaze.utils

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.threeten.bp.LocalDate

class DateConvertersTest {
  @Test
  fun `convert string to date`() {
    val dateString = "2020-02-14"
    val expectedDate = LocalDate.parse("2020-02-14")

    assertThat(DateConverters.toDate(dateString)).isEqualTo(expectedDate)
  }

  @Test
  fun `convert date to string`() {
    val date = LocalDate.parse("2020-01-25")
    val expectedDateString = "2020-01-25"

    assertThat(DateConverters.fromDate(date)).isEqualTo(expectedDateString)
  }
}
