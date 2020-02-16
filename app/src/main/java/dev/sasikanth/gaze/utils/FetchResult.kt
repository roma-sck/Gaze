package dev.sasikanth.gaze.utils

sealed class FetchResult {
  object UnInitialized : FetchResult()
  object Success : FetchResult()
  data class Fail(val error: String) : FetchResult()
  object Loading : FetchResult()
}
