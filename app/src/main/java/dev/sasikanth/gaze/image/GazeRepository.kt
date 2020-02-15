package dev.sasikanth.gaze.image

import dev.sasikanth.gaze.BuildConfig
import kotlinx.coroutines.flow.Flow
import org.threeten.bp.LocalDate

class GazeRepository(
  private val imageDao: GazeImage.RoomDao,
  private val pictureApi: NasaPictureApi
) {
  private val apiKey = BuildConfig.API_KEY

  fun loadImages(): Flow<List<GazeImage>> =
    imageDao.images()

  suspend fun fetchPictures(startDate: LocalDate, endDate: LocalDate): List<GazeImage> =
    pictureApi.getPictures(apiKey, startDate.toString(), endDate.toString())

  fun insertPictures(pictures: List<GazeImage>) =
    imageDao.insert(pictures)
}
