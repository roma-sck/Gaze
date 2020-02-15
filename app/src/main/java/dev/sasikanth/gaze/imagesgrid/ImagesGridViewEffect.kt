package dev.sasikanth.gaze.imagesgrid

import org.threeten.bp.LocalDate

sealed class ImagesGridViewEffect

data class OpenImageDetailsPage(val date: LocalDate) : ImagesGridViewEffect()
