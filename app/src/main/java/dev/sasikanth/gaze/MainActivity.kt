package dev.sasikanth.gaze

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.spotify.mobius.Mobius
import com.spotify.mobius.android.MobiusLoopViewModel
import com.spotify.mobius.functions.Function
import dev.sasikanth.gaze.di.injector
import dev.sasikanth.gaze.image.GazeRepository
import dev.sasikanth.gaze.imagesgrid.ImagesGridEffect
import dev.sasikanth.gaze.imagesgrid.ImagesGridEffectHandler
import dev.sasikanth.gaze.imagesgrid.ImagesGridEvent
import dev.sasikanth.gaze.imagesgrid.ImagesGridInit
import dev.sasikanth.gaze.imagesgrid.ImagesGridModel
import dev.sasikanth.gaze.imagesgrid.ImagesGridUpdate
import dev.sasikanth.gaze.imagesgrid.ImagesGridViewEffect
import dev.sasikanth.gaze.utils.DispatcherProvider
import org.threeten.bp.Clock
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

  @Inject
  lateinit var pacificClock: Clock

  @Inject
  lateinit var repository: GazeRepository

  @Inject
  lateinit var dispatcherProvider: DispatcherProvider

  private lateinit var viewModel: MobiusLoopViewModel<ImagesGridModel, ImagesGridEvent, ImagesGridEffect, ImagesGridViewEffect>

  private val viewModelFactory = object : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
      return MobiusLoopViewModel.create<ImagesGridModel, ImagesGridEvent, ImagesGridEffect, ImagesGridViewEffect>(
        Function {
          return@Function Mobius.loop(
            ImagesGridUpdate(pacificClock),
            ImagesGridEffectHandler(repository, dispatcherProvider)
          )
        },
        ImagesGridModel.create(numberOfImagesToLoad = 15),
        ImagesGridInit()
      ) as T
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    injector.inject(this)
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    viewModel = ViewModelProvider(this, viewModelFactory).get()
  }
}
