package dev.sasikanth.gaze.di.app

import dagger.Module
import dagger.Provides
import java.time.format.DateTimeFormatter
import java.util.Locale

@Module
object DateFormatterModule {

    @Provides
    fun dateFormatter(): DateTimeFormatter =
        DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.getDefault())
}
