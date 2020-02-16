package dev.sasikanth.gaze.imagesgrid

class ImagesGridUiRenderer(
  private val ui: ImagesGridUi
) {

  fun render(model: ImagesGridModel) {
    if (model.images.isNullOrEmpty()) {
      ui.showProgress()
    } else {
      val gridImages = model.images.map(::ImageGridItem)
      val gridItems = gridImages + listOf(ProgressGridItem)

      ui.hideProgress()
      ui.showImages(gridItems)
    }
  }
}
