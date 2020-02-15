package dev.sasikanth.gaze.imagesgrid

import com.spotify.mobius.test.NextMatchers.hasEffects
import com.spotify.mobius.test.NextMatchers.hasModel
import com.spotify.mobius.test.NextMatchers.hasNoEffects
import com.spotify.mobius.test.NextMatchers.hasNoModel
import com.spotify.mobius.test.UpdateSpec
import com.spotify.mobius.test.UpdateSpec.assertThatNext
import org.junit.Test
import org.threeten.bp.LocalDate

class ImagesGridUpdateTest {
  private val defaultModel = ImagesGridModel.create(numberOfImagesToLoad = 15)
  private val updateSpec = UpdateSpec<ImagesGridModel, ImagesGridEvent, ImagesGridEffect>(ImagesGridUpdate())

  @Test
  fun `when images are loaded, then update ui`() {
    val date = LocalDate.parse("2020-02-15")
    val image = GazeImage(date)
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

  @Test
  fun `when fetch images is successful, then update ui`() {
    updateSpec
      .given(defaultModel)
      .whenEvent(FetchImagesSuccess)
      .then(
        assertThatNext(
          hasModel(defaultModel.fetchImagesSuccess()),
          hasNoEffects()
        )
      )
  }

  @Test
  fun `when fetch images is unsuccessful, then update ui`() {
    val errorMessage = "Failed to load images"

    updateSpec
      .given(defaultModel)
      .whenEvent(FetchImagesFail(errorMessage))
      .then(
        assertThatNext(
          hasModel(defaultModel.fetchImagesFail(errorMessage))
        )
      )
  }

  @Test
  fun `when image is clicked, then show image details`() {
    val date = LocalDate.parse("2020-02-14")
    val image = GazeImage(date)
    val images = listOf(image)

    updateSpec
      .given(defaultModel.imagesLoaded(images))
      .whenEvent(ImageClicked(date))
      .then(
        assertThatNext(
          hasNoModel(),
          hasEffects(ShowImageDetails(date) as ImagesGridEffect)
        )
      )
  }

  @Test
  fun `when images list reaches end, then fetch more images`() {
    val image1 = GazeImage(LocalDate.parse("2018-01-02"))
    val image2 = GazeImage(LocalDate.parse("2018-01-01"))
    val images = listOf(image1, image2)

    val expectedStartDate = LocalDate.parse("2017-12-16")
    val expectedEndDate = LocalDate.parse("2017-12-31")

    updateSpec
      .given(defaultModel.imagesLoaded(images))
      .whenEvent(ImagesListReachedEnd)
      .then(
        assertThatNext(
          hasNoModel(),
          hasEffects(FetchMoreImages(expectedStartDate, expectedEndDate) as ImagesGridEffect)
        )
      )
  }
}
