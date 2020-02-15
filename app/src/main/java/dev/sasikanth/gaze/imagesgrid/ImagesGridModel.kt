package dev.sasikanth.gaze.imagesgrid

import dev.sasikanth.gaze.utils.Result
import dev.sasikanth.gaze.utils.Result.Fail
import dev.sasikanth.gaze.utils.Result.Success
import dev.sasikanth.gaze.utils.Result.UnInitialized

data class ImagesGridModel(
  val images: List<GazeImage>?,
  val numberOfImagesToLoad: Int,
  val fetchImagesStatus: Result
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
