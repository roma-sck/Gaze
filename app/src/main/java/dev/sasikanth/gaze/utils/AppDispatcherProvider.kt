package dev.sasikanth.gaze.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class AppDispatcherProvider : DispatcherProvider {
  override val main: CoroutineDispatcher = Dispatchers.Main.immediate
  override val io: CoroutineDispatcher = Dispatchers.IO
  override val default: CoroutineDispatcher = Dispatchers.Default
}
