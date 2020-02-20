package dev.sasikanth.gaze.imagesgrid

import org.threeten.bp.LocalDate

sealed class ImagesGridEffect

object LoadImages : ImagesGridEffect()

data class FetchImages(val startDate: LocalDate, val endDate: LocalDate) : ImagesGridEffect()

data class ShowImageDetails(val date: LocalDate) : ImagesGridEffect()

data class FetchMoreImages(val startDate: LocalDate, val endDate: LocalDate) : ImagesGridEffect()

data class RetryFetchMoreImages(val startDate: LocalDate, val endDate: LocalDate) : ImagesGridEffect()

data class RetryFetchImages(val startDate: LocalDate, val endDate: LocalDate) : ImagesGridEffect()
