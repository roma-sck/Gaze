package dev.sasikanth.gaze.imagesgrid

import com.spotify.mobius.First
import com.spotify.mobius.Init
import dev.sasikanth.gaze.utils.first

class ImagesGridInit : Init<ImagesGridModel, ImagesGridEffect> {
  override fun init(model: ImagesGridModel): First<ImagesGridModel, ImagesGridEffect> {
    return first(model, LoadImages)
  }
}
