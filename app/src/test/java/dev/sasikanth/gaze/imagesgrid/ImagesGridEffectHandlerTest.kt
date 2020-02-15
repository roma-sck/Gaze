package dev.sasikanth.gaze.imagesgrid

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.spotify.mobius.Connection
import com.spotify.mobius.test.RecordingConsumer
import dev.sasikanth.gaze.image.GazeImage
import dev.sasikanth.gaze.image.GazeRepository
import dev.sasikanth.gaze.utils.TestDispatcherProvider
import kotlinx.coroutines.flow.flowOf
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.threeten.bp.LocalDate

class ImagesGridEffectHandlerTest {
  private val gazeRepository = mock<GazeRepository>()
  private val testDispatcherProvider = TestDispatcherProvider()
  private val consumer = RecordingConsumer<ImagesGridEvent>()
  private val effectHandler = ImagesGridEffectHandler(gazeRepository, testDispatcherProvider)

  private lateinit var connection: Connection<ImagesGridEffect>

  @Before
  fun setup() {
    connection = effectHandler.connect(consumer)
  }

  @Test
  fun `load images, when load images effect is received`() {
    val image1 = GazeImage(LocalDate.parse("2018-01-02"))
    val image2 = GazeImage(LocalDate.parse("2018-01-01"))
    val images = listOf(image1, image2)

    whenever(gazeRepository.loadImages()) doReturn flowOf(images)

    connection.accept(LoadImages)

    consumer.assertValues(ImagesLoaded(images))
  }

  @After
  fun tearDown() {
    connection.dispose()
  }
}
