package dev.sasikanth.gaze.utils

sealed class Result {
  object UnInitialized : Result()
  object Success : Result()
  data class Fail(val error: String) : Result()
}
