package dev.sasikanth.gaze.imagesgrid

data class ImagesGridModel(
  val images: List<GazeImage>?,
  val numberOfImagesToLoad: Int
) {
  companion object {
    fun create(numberOfImagesToLoad: Int) = ImagesGridModel(null, numberOfImagesToLoad)
  }

  fun imagesLoaded(images: List<GazeImage>): ImagesGridModel =
    copy(images = images)
}
