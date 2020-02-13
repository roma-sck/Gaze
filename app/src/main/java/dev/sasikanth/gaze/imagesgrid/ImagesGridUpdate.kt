package dev.sasikanth.gaze.imagesgrid

import com.spotify.mobius.Next
import com.spotify.mobius.Next.next
import com.spotify.mobius.Update

class ImagesGridUpdate : Update<ImagesGridModel, ImagesGridEvent, ImagesGridEffect> {
  override fun update(model: ImagesGridModel, event: ImagesGridEvent): Next<ImagesGridModel, ImagesGridEffect> {
    return when (event) {
      is ImagesLoaded -> next(model.imagesLoaded(event.images))
    }
  }
}
