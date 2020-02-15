package dev.sasikanth.gaze.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import dev.sasikanth.gaze.MainActivity
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {

  @Component.Factory
  interface Factory {
    fun create(@BindsInstance context: Context): AppComponent
  }

  fun inject(mainActivity: MainActivity)
}
