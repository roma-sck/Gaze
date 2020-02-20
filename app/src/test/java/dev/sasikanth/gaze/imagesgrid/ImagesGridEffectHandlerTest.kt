package dev.sasikanth.gaze.imagesgrid

import com.nhaarman.mockitokotlin2.doNothing
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.doThrow
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.spotify.mobius.Connection
import com.spotify.mobius.test.RecordingConsumer
import dev.sasikanth.gaze.image.GazeRepository
import dev.sasikanth.gaze.utils.ImageMocker
import dev.sasikanth.gaze.utils.TestDispatcherProvider
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.threeten.bp.LocalDate

class ImagesGridEffectHandlerTest {
  private val gazeRepository = mock<GazeRepository>()
  private val testDispatcherProvider = TestDispatcherProvider()
  private val consumer = RecordingConsumer<ImagesGridEvent>()
  private val viewEffectsConsumer = RecordingConsumer<ImagesGridViewEffect>()
  private val effectHandler = ImagesGridEffectHandler(gazeRepository, testDispatcherProvider, viewEffectsConsumer)

  private lateinit var connection: Connection<ImagesGridEffect>

  @Before
  fun setup() {
    connection = effectHandler.connect(consumer)
  }

  @Test
  fun `load images, when load images effect is received`() {
    val image1 = ImageMocker.image(LocalDate.parse("2018-01-02"))
    val image2 = ImageMocker.image(LocalDate.parse("2018-01-01"))
    val images = listOf(image1, image2)

    whenever(gazeRepository.loadImages()) doReturn flowOf(images)

    connection.accept(LoadImages)

    consumer.assertValues(ImagesLoaded(images))
  }

  @Test
  fun `fetch images, when fetch images effect is received`() = runBlockingTest {
    val startDate = LocalDate.parse("2018-01-01")
    val endDate = LocalDate.parse("2018-01-05")

    val image1 = ImageMocker.image(LocalDate.parse("2018-01-01"))
    val image2 = ImageMocker.image(LocalDate.parse("2018-01-02"))
    val image3 = ImageMocker.image(LocalDate.parse("2018-01-03"))
    val image4 = ImageMocker.image(LocalDate.parse("2018-01-04"))
    val image5 = ImageMocker.image(LocalDate.parse("2018-01-05"))

    whenever(gazeRepository.fetchPictures(startDate, endDate)) doReturn listOf(image1, image2, image3, image4, image5)

    connection.accept(FetchImages(startDate, endDate))

    consumer.assertValues(FetchImagesSuccess)
  }

  @Test
  fun `handle failure when fetching images`() = runBlockingTest {
    val startDate = LocalDate.parse("2018-01-01")
    val endDate = LocalDate.parse("2018-01-03")

    whenever(gazeRepository.fetchPictures(startDate, endDate)) doThrow NullPointerException("Failed to load images")

    connection.accept(FetchImages(startDate, endDate))

    consumer.assertValues(FetchImagesFail("Failed to load images"))
  }

  @Test
  fun `fetch more images, when fetch more images effect is received`() = runBlockingTest {
    val startDate = LocalDate.parse("2018-01-01")
    val endDate = LocalDate.parse("2018-01-05")

    val image1 = ImageMocker.image(LocalDate.parse("2018-01-01"))
    val image2 = ImageMocker.image(LocalDate.parse("2018-01-02"))
    val image3 = ImageMocker.image(LocalDate.parse("2018-01-03"))
    val image4 = ImageMocker.image(LocalDate.parse("2018-01-04"))
    val image5 = ImageMocker.image(LocalDate.parse("2018-01-05"))

    whenever(gazeRepository.fetchPictures(startDate, endDate)) doReturn listOf(image1, image2, image3, image4, image5)

    connection.accept(FetchMoreImages(startDate, endDate))

    consumer.assertValues(FetchMoreImagesSuccess)
  }

  @Test
  fun `handle failure when fetching more images`() = runBlockingTest {
    val startDate = LocalDate.parse("2018-01-01")
    val endDate = LocalDate.parse("2018-01-03")

    whenever(gazeRepository.fetchPictures(startDate, endDate)) doThrow NullPointerException("Failed to load images")

    connection.accept(FetchMoreImages(startDate, endDate))

    consumer.assertValues(FetchMoreImagesFail("Failed to load images"))
  }

  @Test
  fun `open image details page, when show image detail page effect is received`() {
    val image = ImageMocker.image(LocalDate.parse("2028-01-22"))

    connection.accept(ShowImageDetails(image.date))

    viewEffectsConsumer.assertValues(OpenImageDetailsPage(image.date))
  }

  @Test
  fun `retry fetching more images, when retry fetch more images effect is received`() = runBlockingTest {
    val startDate = LocalDate.parse("2018-01-01")
    val endDate = LocalDate.parse("2018-01-05")

    val image1 = ImageMocker.image(LocalDate.parse("2018-01-01"))
    val image2 = ImageMocker.image(LocalDate.parse("2018-01-02"))
    val image3 = ImageMocker.image(LocalDate.parse("2018-01-03"))
    val image4 = ImageMocker.image(LocalDate.parse("2018-01-04"))
    val image5 = ImageMocker.image(LocalDate.parse("2018-01-05"))

    whenever(gazeRepository.fetchPictures(startDate, endDate)) doReturn listOf(image1, image2, image3, image4, image5)

    connection.accept(RetryFetchMoreImages(startDate, endDate))

    consumer.assertValues(FetchMoreImagesSuccess)
  }

  @Test
  fun `retry fetch images, when retry fetch images effect is received`() = runBlockingTest {
    val startDate = LocalDate.parse("2018-01-01")
    val endDate = LocalDate.parse("2018-01-05")

    val image1 = ImageMocker.image(LocalDate.parse("2018-01-01"))
    val image2 = ImageMocker.image(LocalDate.parse("2018-01-02"))

    whenever(gazeRepository.fetchPictures(startDate, endDate)) doReturn listOf(image1, image2)
    doNothing().whenever(gazeRepository).insertPictures(listOf(image1, image2))

    connection.accept(RetryFetchImages(startDate, endDate))

    consumer.assertValues(FetchImagesSuccess)
  }

  @After
  fun tearDown() {
    connection.dispose()
  }
}
