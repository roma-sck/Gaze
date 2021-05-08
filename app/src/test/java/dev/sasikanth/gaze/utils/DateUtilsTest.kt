package dev.sasikanth.gaze.utils

import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate

class DateUtilsTest {

    /**
     * Check if the given date is formatted as MM/dd/yyyy
     */
    @Test
    fun `check if date is formatted to app format`() {
        val date = LocalDate.parse("2021-05-08")
        val formattedDate = DateUtils.formatToAppDate(date)

        assertTrue(formattedDate == "08 May 2021")
    }
}
