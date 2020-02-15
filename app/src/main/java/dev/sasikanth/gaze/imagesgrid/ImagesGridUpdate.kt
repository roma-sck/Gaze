package dev.sasikanth.gaze.imagesgrid

import com.spotify.mobius.Next
import com.spotify.mobius.Update
import dev.sasikanth.gaze.utils.dispatch
import dev.sasikanth.gaze.utils.next

class ImagesGridUpdate : Update<ImagesGridModel, ImagesGridEvent, ImagesGridEffect> {
  override fun update(model: ImagesGridModel, event: ImagesGridEvent): Next<ImagesGridModel, ImagesGridEffect> {
    return when (event) {
      is ImagesLoaded -> {
        if (event.images.isNotEmpty()) {
          next(model.imagesLoaded(event.images))
        } else {
          dispatch<ImagesGridModel, ImagesGridEffect>(FetchImages(model.numberOfImagesToLoad))
        }
      }
      is FetchImagesSuccess -> {
        next(model.fetchImagesSuccess())
      }
      is FetchImagesFail -> {
        next(model.fetchImagesFail(event.errorMessage))
      }
    }
  }
}
