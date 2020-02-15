package dev.sasikanth.gaze.image

import kotlinx.coroutines.flow.Flow

class GazeRepository(
  private val imageDao: GazeImage.RoomDao
) {
  fun loadImages(): Flow<List<GazeImage>> =
    imageDao.images()
}
