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

class ImagesGridEffectHandler(
  private val gazeRepository: GazeRepository,
  private val dispatcherProvider: DispatcherProvider
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
              try {
                val pictures = gazeRepository.fetchPictures(effect.startDate, effect.endDate)

                withContext(dispatcherProvider.io) {
                  gazeRepository.insertPictures(pictures)
                }
                output.accept(FetchImagesSuccess)
              } catch (e: Exception) {
                output.accept(FetchImagesFail(e.localizedMessage.orEmpty()))
              }
            }
          }
        }
      }

      override fun dispose() {
        coroutineScope.cancel()
      }
    }
  }
}
