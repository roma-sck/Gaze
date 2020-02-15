package dev.sasikanth.gaze.imagesgrid

import org.threeten.bp.LocalDate

sealed class ImagesGridEffect

object LoadImages : ImagesGridEffect()

data class FetchImages(val numberOfImagesToLoad: Int) : ImagesGridEffect()

data class ShowImageDetails(val date: LocalDate) : ImagesGridEffect()
