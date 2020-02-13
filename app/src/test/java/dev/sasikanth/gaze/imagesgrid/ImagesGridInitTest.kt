package dev.sasikanth.gaze.imagesgrid

import com.spotify.mobius.test.FirstMatchers.hasEffects
import com.spotify.mobius.test.FirstMatchers.hasModel
import com.spotify.mobius.test.InitSpec
import com.spotify.mobius.test.InitSpec.assertThatFirst
import org.junit.Test

class ImagesGridInitTest {
  @Test
  fun `when screen is created, then load images`() {
    val defaultModel = ImagesGridModel.DEFAULT
    val initSpec = InitSpec<ImagesGridModel, ImagesGridEffect>(ImagesGridInit())

    initSpec
      .whenInit(defaultModel)
      .then(
        assertThatFirst(
          hasModel(defaultModel),
          hasEffects(LoadImages as ImagesGridEffect)
        )
      )
  }
}
