package dev.sasikanth.gaze.ui.pages.viewer

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.LargeTest
import dev.sasikanth.gaze.R
import org.junit.Test

@LargeTest
class ViewerFragmentTest {

    @Test
    fun isBackButtonDisplayed() {
        launchFragmentInContainer<ViewerFragment>(themeResId = R.style.AppTheme)
        onView(withId(R.id.exit_picture_detail)).check(matches(isDisplayed()))
    }

    @Test
    fun isDownloadButtonDisplayed() {
        launchFragmentInContainer<ViewerFragment>(themeResId = R.style.AppTheme)
        onView(withId(R.id.download_picture)).check(matches(isDisplayed()))
    }

    @Test
    fun isPictureInformationDisplayed() {
        launchFragmentInContainer<ViewerFragment>(themeResId = R.style.AppTheme)
        onView(withId(R.id.expand_picture_information)).check(matches(isDisplayed()))
    }
}
