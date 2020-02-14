package dev.sasikanth.gaze.imagesgrid

import com.spotify.mobius.test.NextMatchers.hasEffects
import com.spotify.mobius.test.NextMatchers.hasModel
import com.spotify.mobius.test.NextMatchers.hasNoEffects
import com.spotify.mobius.test.NextMatchers.hasNoModel
import com.spotify.mobius.test.UpdateSpec
import com.spotify.mobius.test.UpdateSpec.assertThatNext
import org.junit.Test

class ImagesGridUpdateTest {
  private val defaultModel = ImagesGridModel.create(numberOfImagesToLoad = 15)
  private val updateSpec = UpdateSpec<ImagesGridModel, ImagesGridEvent, ImagesGridEffect>(ImagesGridUpdate())

  @Test
  fun `when images are loaded, then update ui`() {
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

  @Test
  fun `when there are no images, then fetch images`() {
    val images = emptyList<GazeImage>()

    updateSpec
      .given(defaultModel)
      .whenEvent(ImagesLoaded(images))
      .then(
        assertThatNext(
          hasNoModel(),
          hasEffects(FetchImages(defaultModel.numberOfImagesToLoad) as ImagesGridEffect)
        )
      )
  }
}
