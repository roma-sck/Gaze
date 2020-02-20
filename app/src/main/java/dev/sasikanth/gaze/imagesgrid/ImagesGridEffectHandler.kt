package dev.sasikanth.gaze.imagesgrid

import com.spotify.mobius.Connectable
import com.spotify.mobius.Connection
import com.spotify.mobius.functions.Consumer
import dev.sasikanth.gaze.image.GazeRepository
import dev.sasikanth.gaze.utils.DispatcherProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.LocalDate

class ImagesGridEffectHandler(
  private val gazeRepository: GazeRepository,
  private val dispatcherProvider: DispatcherProvider,
  val viewEffects: Consumer<ImagesGridViewEffect>
) : Connectable<ImagesGridEffect, ImagesGridEvent> {

  override fun connect(output: Consumer<ImagesGridEvent>): Connection<ImagesGridEffect> {
    val coroutineScope = CoroutineScope(dispatcherProvider.main)

    return object : Connection<ImagesGridEffect> {
      override fun accept(effect: ImagesGridEffect) {
        when (effect) {
          is LoadImages -> {
            coroutineScope.launch {
              gazeRepository.loadImages().collect { images ->
                output.accept(ImagesLoaded(images))
              }
            }
          }
          is FetchImages -> {
            coroutineScope.launch {
              fetchImages(effect.startDate, effect.endDate, output)
            }
          }
          is FetchMoreImages -> {
            coroutineScope.launch {
              fetchMoreImages(effect.startDate, effect.endDate, output)
            }
          }
          is ShowImageDetails -> {
            viewEffects.accept(OpenImageDetailsPage(effect.date))
          }
          is RetryFetchMoreImages -> {
            coroutineScope.launch {
              fetchMoreImages(effect.startDate, effect.endDate, output)
            }
          }
          is RetryFetchImages -> {
            coroutineScope.launch {
              fetchImages(effect.startDate, effect.endDate, output)
            }
          }
        }
      }

      override fun dispose() {
        coroutineScope.cancel()
      }
    }
  }

  // TODO: Get opinion on these methods
  private suspend fun fetchImages(
    startDate: LocalDate,
    endDate: LocalDate,
    output: Consumer<ImagesGridEvent>
  ) {
    try {
      val pictures = gazeRepository.fetchPictures(startDate, endDate)

      withContext(dispatcherProvider.io) {
        gazeRepository.insertPictures(pictures)
      }
      output.accept(FetchImagesSuccess)
    } catch (e: Exception) {
      output.accept(FetchImagesFail(e.localizedMessage.orEmpty()))
    }
  }

  private suspend fun fetchMoreImages(
    startDate: LocalDate,
    endDate: LocalDate,
    output: Consumer<ImagesGridEvent>
  ) {
    try {
      val pictures = gazeRepository.fetchPictures(startDate, endDate)

      withContext(dispatcherProvider.io) {
        gazeRepository.insertPictures(pictures)
      }
      output.accept(FetchMoreImagesSuccess)
    } catch (e: Exception) {
      output.accept(FetchMoreImagesFail(e.localizedMessage.orEmpty()))
    }
  }
}
