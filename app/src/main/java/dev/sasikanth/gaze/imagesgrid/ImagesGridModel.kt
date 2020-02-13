package dev.sasikanth.gaze.imagesgrid

data class ImagesGridModel(val images: List<GazeImage>?) {
  companion object {
    val DEFAULT = ImagesGridModel(null)
  }
}
