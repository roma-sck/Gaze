package dev.sasikanth.gaze

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.sasikanth.gaze.image.GazeImage
import dev.sasikanth.gaze.utils.DateConverters

@Database(
  entities = [GazeImage::class],
  version = 1,
  exportSchema = false
)
@TypeConverters(DateConverters::class)
abstract class AppDatabase : RoomDatabase() {
  abstract fun gazeDao(): GazeImage.RoomDao
}
