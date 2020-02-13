package dev.sasikanth.gaze.imagesgrid

sealed class ImagesGridEvent

data class ImagesLoaded(val images: List<GazeImage>) : ImagesGridEvent()
