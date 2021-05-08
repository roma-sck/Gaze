package dev.sasikanth.gaze.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.sasikanth.gaze.data.APod
import dev.sasikanth.gaze.utils.LocalDateTypeConverter

@Database(entities = [APod::class], version = 1, exportSchema = false)
@TypeConverters(LocalDateTypeConverter::class)
abstract class APodDatabase : RoomDatabase() {

    abstract val aPodDao: APodDao
}
