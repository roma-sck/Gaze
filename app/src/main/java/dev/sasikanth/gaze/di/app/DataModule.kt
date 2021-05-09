package dev.sasikanth.gaze.di.app

import android.app.Application
import androidx.room.Room
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.sasikanth.gaze.data.source.local.APodDatabase
import dev.sasikanth.gaze.data.source.remote.APodApiService
import dev.sasikanth.gaze.utils.GsonLocalDateAdapter
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.time.LocalDate
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Singleton
    @Provides
    fun providesAPodDatabase(application: Application): APodDatabase = Room
        .databaseBuilder(
            application,
            APodDatabase::class.java,
            "apod.db"
        )
        .fallbackToDestructiveMigration()
        .build()

    @Singleton
    @Provides
    fun providesAPodDao(database: APodDatabase) = database.aPodDao

    @Singleton
    @Provides
    fun providesRetrofit(): Retrofit {
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

        // Creating Gson instance with GsonDateAdapter type adapter
        val gson = GsonBuilder()
            .registerTypeAdapter(LocalDate::class.java, GsonLocalDateAdapter())
            .create()

        return Retrofit.Builder()
            .baseUrl("https://api.nasa.gov/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Singleton
    @Provides
    fun providesAPodApiService(retrofit: Retrofit): APodApiService = retrofit.create()
}
