package dev.sasikanth.gaze.imagesgrid

data class ImagesGridModel(val images: List<GazeImage>?) {
  companion object {
    val DEFAULT = ImagesGridModel(null)
  }

  fun imagesLoaded(images: List<GazeImage>): ImagesGridModel =
    copy(images = images)
}
