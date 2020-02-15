package dev.sasikanth.gaze.di

import android.content.Context
import androidx.room.Room
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dev.sasikanth.gaze.AppDatabase
import dev.sasikanth.gaze.image.NasaPictureApi
import dev.sasikanth.gaze.utils.AppDispatcherProvider
import dev.sasikanth.gaze.utils.DateConverters
import dev.sasikanth.gaze.utils.DispatcherProvider
import dev.sasikanth.gaze.utils.PacificClock
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.threeten.bp.Clock
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit
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
    // Creating log interceptors with basic level
    val interceptor = HttpLoggingInterceptor()
    interceptor.level = HttpLoggingInterceptor.Level.BASIC

    // OkHttp Client
    // Adding log interceptors and setting timeouts
    val client = OkHttpClient.Builder()
      .addInterceptor(interceptor)
      .readTimeout(2, TimeUnit.MINUTES)
      .connectTimeout(2, TimeUnit.MINUTES)
      .build()

    val moshi = Moshi.Builder()
      .add(DateConverters)
      .build()

    val retrofit = Retrofit.Builder()
      .baseUrl("https://api.nasa.gov/")
      .client(client)
      .addConverterFactory(MoshiConverterFactory.create(moshi))
      .build()

    return retrofit.create()
  }

  @Provides
  @Singleton
  fun providesAppDispatcherProvider(): DispatcherProvider {
    return AppDispatcherProvider()
  }

  @Provides
  @Singleton
  fun providesPacificClock(): Clock {
    return PacificClock()
  }
}
