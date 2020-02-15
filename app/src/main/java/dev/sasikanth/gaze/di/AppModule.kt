package dev.sasikanth.gaze.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dev.sasikanth.gaze.AppDatabase
import dev.sasikanth.gaze.image.NasaPictureApi
import dev.sasikanth.gaze.utils.AppDispatcherProvider
import dev.sasikanth.gaze.utils.DispatcherProvider
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
object AppModule {

  @Provides
  @Singleton
  fun providesAppDatabase(context: Context): AppDatabase {
    return Room.databaseBuilder(context, AppDatabase::class.java, "gaze")
      .fallbackToDestructiveMigration()
      .build()
  }

  @Provides
  @Singleton
  fun providesNasaPicturesApi(): NasaPictureApi {
    val retrofit = Retrofit.Builder()
      .baseUrl("https://api.nasa.gov/")
      .addConverterFactory(MoshiConverterFactory.create())
      .build()

    return retrofit.create()
  }

  @Provides
  @Singleton
  fun providesAppDispatcherProvider(): DispatcherProvider {
    return AppDispatcherProvider()
  }
}
