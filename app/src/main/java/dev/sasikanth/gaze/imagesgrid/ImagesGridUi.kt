package dev.sasikanth.gaze.imagesgrid

interface ImagesGridUi {
  fun showProgress()
  fun hideProgress()
  fun showImages(gridItems: List<GridItem>)
  fun showError(error: String)
  fun hideError()
}
