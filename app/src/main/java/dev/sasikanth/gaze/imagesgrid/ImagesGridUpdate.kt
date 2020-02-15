package dev.sasikanth.gaze.imagesgrid

import com.spotify.mobius.Next
import com.spotify.mobius.Update
import dev.sasikanth.gaze.utils.dispatch
import dev.sasikanth.gaze.utils.next
import org.threeten.bp.Clock
import org.threeten.bp.LocalDate

class ImagesGridUpdate(private val clock: Clock) : Update<ImagesGridModel, ImagesGridEvent, ImagesGridEffect> {
  override fun update(model: ImagesGridModel, event: ImagesGridEvent): Next<ImagesGridModel, ImagesGridEffect> {
    return when (event) {
      is ImagesLoaded -> {
        if (event.images.isNotEmpty()) {
          fetchLatestImagesIfNecessary(event, model)
        } else {
          fetchImagesWhenEmpty(model)
        }
      }
      is FetchImagesSuccess -> {
        next(model.fetchImagesSuccess())
      }
      is FetchImagesFail -> {
        next(model.fetchImagesFail(event.errorMessage))
      }
      is ImageClicked -> {
        dispatch(ShowImageDetails(event.date))
      }
      is ImagesListReachedEnd -> {
        val images = model.images!!
        // Subtracting 1 day to make sure we are not fetching the last image date again
        val endDate = images.last().date.minusDays(1)
        val startDate = endDate.minusDays(model.numberOfImagesToLoad.toLong())

        dispatch(FetchMoreImages(startDate, endDate))
      }
      is FetchMoreImagesSuccess -> {
        next(model.fetchMoreImageSuccess())
      }
    }
  }

  private fun fetchLatestImagesIfNecessary(
    event: ImagesLoaded,
    model: ImagesGridModel
  ): Next<ImagesGridModel, ImagesGridEffect> {
    val currentDate = LocalDate.now(clock)
    val latestImageDate = event.images.first().date
    val imagesLoadedModel = model.imagesLoaded(event.images)

    return if (latestImageDate < currentDate) {
      // Adding a day to latest image day to avoid fetching the latest image date again
      val startDate = latestImageDate.plusDays(1)

      next(imagesLoadedModel, FetchImages(startDate, currentDate))
    } else {
      next(imagesLoadedModel)
    }
  }

  private fun fetchImagesWhenEmpty(model: ImagesGridModel): Next<ImagesGridModel, ImagesGridEffect> {
    val endDate = LocalDate.now(clock)
    val startDate = endDate.minusDays(model.numberOfImagesToLoad.toLong())
    return dispatch(FetchImages(startDate, endDate))
  }
}
