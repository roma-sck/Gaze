package dev.sasikanth.gaze.imagesgrid

sealed class ImagesGridEffect

object LoadImages : ImagesGridEffect()

data class FetchImages(val numberOfImagesToLoad: Int) : ImagesGridEffect()
