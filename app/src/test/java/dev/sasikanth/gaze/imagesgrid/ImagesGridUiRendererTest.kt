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
  fun `when there are no images and fetch images status is loading then show progress`() {
    // given
    val model = ImagesGridModel.create(numberOfImagesToLoad = 15)
      .imagesLoaded(emptyList())
      .fetchingImages()

    // when
    uiRenderer.render(model)

    // then
    verify(ui).showProgress()
    verify(ui).hideError()
    verifyNoMoreInteractions(ui)
  }

  @Test
  fun `when images are loaded then hide progress and error message and show images`() {
    // given
    val image = ImageMocker.image(LocalDate.parse("2018-01-01"))
    val images = listOf(image)
    val model = ImagesGridModel.create(numberOfImagesToLoad = 15)
      .imagesLoaded(images)

    val gridItems = listOf(ImageGridItem(image))

    // when
    uiRenderer.render(model)

    // then
    verify(ui).hideProgress()
    verify(ui).hideError()
    verify(ui).showImages(gridItems)
    verifyNoMoreInteractions(ui)
  }

  @Test
  fun `when fetch more images status is failed, then show error grid item`() {
    // given
    val errorMessage = "Failed to load images"
    val image = ImageMocker.image(LocalDate.parse("2020-01-23"))
    val images = listOf(image)
    val model = ImagesGridModel.create(numberOfImagesToLoad = 15)
      .imagesLoaded(images)
      .fetchMoreImagesFail(errorMessage)

    val expectedGridItems = listOf(ImageGridItem(image)) + listOf(ErrorGridItem(errorMessage))

    // when
    uiRenderer.render(model)

    // then
    verify(ui).hideProgress()
    verify(ui).hideError()
    verify(ui).showImages(expectedGridItems)
    verifyNoMoreInteractions(ui)
  }

  @Test
  fun `when fetching more images, then show progress view in grid`() {
    // given
    val image = ImageMocker.image(LocalDate.parse("2020-02-14"))
    val images = listOf(image)
    val model = ImagesGridModel.create(numberOfImagesToLoad = 15)
      .imagesLoaded(images)
      .fetchingMoreImages()

    val expectedGridItems = listOf(ImageGridItem(image)) + listOf(ProgressGridItem)

    // when
    uiRenderer.render(model)

    // then
    verify(ui).hideProgress()
    verify(ui).hideError()
    verify(ui).showImages(expectedGridItems)
    verifyNoMoreInteractions(ui)
  }

  @Test
  fun `when fetch images error fails and there are no images in model, then show error`() {
    // given
    val model = ImagesGridModel.create(numberOfImagesToLoad = 15)
      .fetchImagesFail("Failed to fetch images")

    // when
    uiRenderer.render(model)

    // then
    verify(ui).hideProgress()
    verify(ui).showError("Failed to fetch images")
    verifyNoMoreInteractions(ui)
  }
}
