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
      val gridItems = when (model.fetchMoreImagesStatus) {
        is FetchResult.Fail -> {
          gridImages + listOf(ErrorGridItem(model.fetchMoreImagesStatus.error))
        }
        is FetchResult.Loading -> {
          gridImages + listOf(ProgressGridItem)
        }
        else -> {
          gridImages
        }
      }

      ui.hideProgress()
      ui.showImages(gridItems)
    }
  }
}
