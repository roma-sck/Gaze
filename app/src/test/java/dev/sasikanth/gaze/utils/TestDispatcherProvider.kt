package dev.sasikanth.gaze.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineDispatcher

class TestDispatcherProvider : DispatcherProvider {
  override val main: CoroutineDispatcher = TestCoroutineDispatcher()
  override val io: CoroutineDispatcher = TestCoroutineDispatcher()
  override val default: CoroutineDispatcher = TestCoroutineDispatcher()
}
