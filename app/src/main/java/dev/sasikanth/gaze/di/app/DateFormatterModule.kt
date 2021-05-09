package dev.sasikanth.gaze.di.app

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.time.format.DateTimeFormatter
import java.util.Locale

@Module
@InstallIn(SingletonComponent::class)
object DateFormatterModule {

    @Provides
    fun dateFormatter(): DateTimeFormatter =
        DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.getDefault())
}
