package dev.sasikanth.gaze.ui.pages.grid

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.LargeTest
import dev.sasikanth.gaze.R
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

@LargeTest
class PicturesGridFragmentTest {

    @Test
    fun checkIfAPodsGrid_isDisplayed() {
        launchFragmentInContainer<PicturesGridFragment>(themeResId = R.style.AppTheme)
        onView(withId(R.id.apods_grid)).check(matches(isDisplayed()))
    }

    @Test
    fun changeCurrentPicturePosition() {
        val defaultPosition = 0
        val changePosition = 5

        with(launchFragmentInContainer<PicturesGridFragment>(themeResId = R.style.AppTheme)) {
            // Checking default value of current picture position
            onFragment { fragment ->
                assertEquals(defaultPosition, fragment.viewModel.currentPicturePosition)
                // Assigning change position to current picture position
                fragment.viewModel.currentPicturePosition = changePosition
            }
            // Recreating to make sure ViewModel SavedState works
            recreate()
            onFragment { fragment ->
                assertEquals(changePosition, fragment.viewModel.currentPicturePosition)
            }
        }
    }

    @Test
    fun changeCurrentPositionToNegativeValue() {
        val defaultPosition = 0
        val changePosition = -5

        with(launchFragmentInContainer<PicturesGridFragment>(themeResId = R.style.AppTheme)) {
            onFragment { fragment ->
                fragment.viewModel.currentPicturePosition = changePosition
                assertNotEquals(changePosition, fragment.viewModel.currentPicturePosition)
                assertEquals(defaultPosition, fragment.viewModel.currentPicturePosition)
            }
        }
    }
}
