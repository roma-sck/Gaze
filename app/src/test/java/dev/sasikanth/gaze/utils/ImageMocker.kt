package dev.sasikanth.gaze.utils

import dev.sasikanth.gaze.image.GazeImage
import org.threeten.bp.LocalDate

object ImageMocker {
  fun image(
    date: LocalDate,
    title: String = "Image Title",
    explanation: String = "Image Explanation",
    copyright: String? = null,
    mediaType: String = "image",
    serviceVersion: String? = null,
    thumbnailUrl: String? = null,
    hdUrl: String? = null
  ): GazeImage {
    return GazeImage(
      date = date,
      title = title,
      explanation = explanation,
      copyright = copyright,
      mediaType = mediaType,
      serviceVersion = serviceVersion,
      thumbnailUrl = thumbnailUrl,
      hdUrl = hdUrl
    )
  }
}
