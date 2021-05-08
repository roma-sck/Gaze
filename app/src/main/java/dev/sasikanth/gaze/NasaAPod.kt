package dev.sasikanth.gaze

import android.app.Application
import dev.sasikanth.gaze.di.app.AppComponent
import dev.sasikanth.gaze.di.app.DaggerAppComponent
import dev.sasikanth.gaze.di.misc.ComponentProvider
import timber.log.Timber

class NasaAPod : Application(), ComponentProvider {

    override val component: AppComponent by lazy {
        DaggerAppComponent.factory().create(this)
    }

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}
