package dev.sasikanth.gaze.image

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.threeten.bp.LocalDate

@Entity(tableName = "images")
data class GazeImage(
  @PrimaryKey
  val date: LocalDate
) {

  @Dao
  interface RoomDao {

    @Query("""
      SELECT * FROM images
      WHERE media_type = 'image'
      ORDER BY date(date) DESC
    """)
    fun images(): Flow<List<GazeImage>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(images: List<GazeImage>)
  }
}
