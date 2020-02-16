package dev.sasikanth.gaze.imagesgrid

import dev.sasikanth.gaze.utils.FetchResult

class ImagesGridUiRenderer(
  private val ui: ImagesGridUi
) {

  fun render(model: ImagesGridModel) {
    if (model.images.isNullOrEmpty()) {
      ui.showProgress()
    } else {
      val gridImages = model.images.map(::ImageGridItem)
      val gridItems = if (model.fetchMoreImagesStatus is FetchResult.Fail) {
        gridImages + listOf(ErrorGridItem(model.fetchMoreImagesStatus.error))
      } else {
        gridImages + listOf(ProgressGridItem)
      }

      ui.hideProgress()
      ui.showImages(gridItems)
    }
  }
}
