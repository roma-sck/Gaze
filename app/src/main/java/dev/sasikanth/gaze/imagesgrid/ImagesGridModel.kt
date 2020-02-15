package dev.sasikanth.gaze.imagesgrid

import dev.sasikanth.gaze.utils.FetchResult
import dev.sasikanth.gaze.utils.FetchResult.Fail
import dev.sasikanth.gaze.utils.FetchResult.Success
import dev.sasikanth.gaze.utils.FetchResult.UnInitialized

data class ImagesGridModel(
  val images: List<GazeImage>?,
  val numberOfImagesToLoad: Int,
  val fetchImagesStatus: FetchResult
) {
  companion object {
    fun create(numberOfImagesToLoad: Int) = ImagesGridModel(null, numberOfImagesToLoad, UnInitialized)
  }

  fun imagesLoaded(images: List<GazeImage>): ImagesGridModel =
    copy(images = images)

  fun fetchImagesSuccess(): ImagesGridModel =
    copy(fetchImagesStatus = Success)

  fun fetchImagesFail(errorMessage: String): ImagesGridModel =
    copy(fetchImagesStatus = Fail(errorMessage))
}
