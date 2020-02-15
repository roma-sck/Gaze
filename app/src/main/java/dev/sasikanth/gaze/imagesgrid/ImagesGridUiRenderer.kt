package dev.sasikanth.gaze.imagesgrid

class ImagesGridUiRenderer(
  private val ui: ImagesGridUi
) {

  fun render(model: ImagesGridModel) {
    if (model.images.isNullOrEmpty()) {
      ui.showProgress()
    } else {
      ui.hideProgress()
      ui.showImages(model.images)
    }
  }
}
