package dev.sasikanth.gaze.imagesgrid

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import org.junit.Test

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
}
