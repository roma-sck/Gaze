package dev.sasikanth.gaze

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import dev.sasikanth.gaze.di.AppComponent
import dev.sasikanth.gaze.di.ComponentProvider
import dev.sasikanth.gaze.di.DaggerAppComponent

class GazeApplication : Application(), ComponentProvider {

  override val component: AppComponent by lazy {
    DaggerAppComponent.factory().create(this)
  }

  override fun onCreate() {
    super.onCreate()
    AndroidThreeTen.init(this)
  }
}
