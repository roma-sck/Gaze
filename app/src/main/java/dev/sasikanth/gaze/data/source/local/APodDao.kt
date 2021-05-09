package dev.sasikanth.gaze.data.source.local

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.sasikanth.gaze.data.APod
import java.time.LocalDate

@Dao
interface APodDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAPod(vararg apod: APod)

    @Query("SELECT * FROM apods WHERE date = :date ORDER BY date DESC LIMIT 1")
    suspend fun getAPod(date: LocalDate): APod?

    @Query("SELECT * FROM apods WHERE media_type == 'image' ORDER BY date DESC")
    fun getAPods(): DataSource.Factory<Int, APod>

    @Query("SELECT date FROM apods ORDER BY date DESC LIMIT 1")
    suspend fun getLatestAPodDate(): LocalDate?
}
