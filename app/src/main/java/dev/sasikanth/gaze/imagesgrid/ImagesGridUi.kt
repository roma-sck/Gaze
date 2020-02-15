package dev.sasikanth.gaze.imagesgrid

import dev.sasikanth.gaze.image.GazeImage

interface ImagesGridUi {
  fun showProgress()
  fun hideProgress()
  fun showImages(images: List<GazeImage>)
}
