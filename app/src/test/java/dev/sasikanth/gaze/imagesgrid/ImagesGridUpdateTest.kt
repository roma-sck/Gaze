package dev.sasikanth.gaze.imagesgrid

import com.spotify.mobius.test.NextMatchers.hasModel
import com.spotify.mobius.test.NextMatchers.hasNoEffects
import com.spotify.mobius.test.UpdateSpec
import com.spotify.mobius.test.UpdateSpec.assertThatNext
import org.junit.Test

class ImagesGridUpdateTest {
  @Test
  fun `when images are loaded, then update ui`() {
    val defaultModel = ImagesGridModel.DEFAULT
    val updateSpec = UpdateSpec<ImagesGridModel, ImagesGridEvent, ImagesGridEffect>(ImagesGridUpdate())
    val image = GazeImage()
    val images = listOf(image)

    updateSpec
      .given(defaultModel)
      .whenEvent(ImagesLoaded(images))
      .then(
        assertThatNext(
          hasModel(defaultModel.imagesLoaded(images)),
          hasNoEffects()
        )
      )
  }
}
