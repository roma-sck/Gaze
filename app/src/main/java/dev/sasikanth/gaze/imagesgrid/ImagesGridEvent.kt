package dev.sasikanth.gaze.imagesgrid

import dev.sasikanth.gaze.image.GazeImage
import org.threeten.bp.LocalDate

sealed class ImagesGridEvent

data class ImagesLoaded(val images: List<GazeImage>) : ImagesGridEvent()

object FetchImagesSuccess : ImagesGridEvent()

data class FetchImagesFail(val errorMessage: String) : ImagesGridEvent()

data class ImageClicked(val date: LocalDate) : ImagesGridEvent()

object ImagesListReachedEnd : ImagesGridEvent()

object FetchMoreImagesSuccess : ImagesGridEvent()

data class FetchMoreImagesFail(val errorMessage: String) : ImagesGridEvent()

object RetryFetchMoreImageClicked : ImagesGridEvent()
