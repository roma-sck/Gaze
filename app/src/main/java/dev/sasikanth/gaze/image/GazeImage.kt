package dev.sasikanth.gaze.image

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.coroutines.flow.Flow
import org.threeten.bp.LocalDate

@JsonClass(generateAdapter = true)
@Entity(tableName = "images")
data class GazeImage(
  @PrimaryKey
  val date: LocalDate,
  val title: String,
  val explanation: String,
  val copyright: String?,
  @Json(name = "media_type")
  @ColumnInfo(name = "media_type")
  val mediaType: String?,
  @Json(name = "service_version")
  @ColumnInfo(name = "service_version")
  val serviceVersion: String?,
  @Json(name = "url")
  @ColumnInfo(name = "url")
  val thumbnailUrl: String?,
  @Json(name = "hdurl")
  @ColumnInfo(name = "hd_url")
  val hdUrl: String?
) {

  @Dao
  interface RoomDao {

    @Query(
      """
      SELECT * FROM images
      WHERE media_type = 'image'
      ORDER BY date(date) DESC
    """
    )
    fun images(): Flow<List<GazeImage>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(images: List<GazeImage>)
  }
}
