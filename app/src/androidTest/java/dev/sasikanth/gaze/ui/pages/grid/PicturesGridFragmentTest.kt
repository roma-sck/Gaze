package dev.sasikanth.gaze.ui.pages.grid

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.LargeTest
import com.google.common.truth.Truth.assertThat
import dev.sasikanth.gaze.R
import dev.sasikanth.gaze.ui.adapters.APodsGridAdapter.APodItemViewHolder
import org.junit.Test

@LargeTest
class PicturesGridFragmentTest {

    private val navController = TestNavHostController(
        ApplicationProvider.getApplicationContext()
    )
    private val scenario =
        launchFragmentInContainer<PicturesGridFragment>(themeResId = R.style.AppTheme)

    @Test
    fun checkIfAPodsGrid_isDisplayed() {
        onView(withId(R.id.apods_grid)).check(matches(isDisplayed()))
    }

    @Test
    fun clickingOnPod_ShouldOpenViewer() {
        val expectedPosition = 5

        with(scenario) {
            onFragment { fragment ->
                navController.setGraph(R.navigation.nav_graph)
                Navigation.setViewNavController(fragment.requireView(), navController)
            }

            onView(withId(R.id.apods_grid)).perform(
                actionOnItemAtPosition<APodItemViewHolder>(
                    expectedPosition,
                    click()
                )
            )
            assertThat(navController.currentDestination?.id).isEqualTo(R.id.viewerFragment)
        }
    }
}
