package dev.sasikanth.gaze.imagesgrid

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import dev.sasikanth.gaze.utils.ImageMocker
import org.junit.Test
import org.threeten.bp.LocalDate

class ImagesGridUiRendererTest {
  private val ui = mock<ImagesGridUi>()
  private val uiRenderer = ImagesGridUiRenderer(ui)

  @Test
  fun `when images is null then show progress`() {
    // given
    val model = ImagesGridModel.create(numberOfImagesToLoad = 15)

    // when
    uiRenderer.render(model)

    // then
    verify(ui).showProgress()
    verifyNoMoreInteractions(ui)
  }

  @Test
  fun `when images are empty then show progress`() {
    // given
    val model = ImagesGridModel.create(numberOfImagesToLoad = 15)
      .imagesLoaded(emptyList())

    // when
    uiRenderer.render(model)

    // then
    verify(ui).showProgress()
    verifyNoMoreInteractions(ui)
  }

  @Test
  fun `when images are loaded then hide progress and show images`() {
    // given
    val image = ImageMocker.image(LocalDate.parse("2018-01-01"))
    val images = listOf(image)
    val model = ImagesGridModel.create(numberOfImagesToLoad = 15)
      .imagesLoaded(images)

    // when
    uiRenderer.render(model)

    // then
    verify(ui).hideProgress()
    verify(ui).showImages(images)
    verifyNoMoreInteractions(ui)
  }
}
