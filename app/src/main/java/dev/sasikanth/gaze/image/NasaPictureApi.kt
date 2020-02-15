package dev.sasikanth.gaze.image

import retrofit2.http.GET
import retrofit2.http.Query

interface NasaPictureApi {
  @GET("planetary/apod")
  suspend fun getPictures(
    @Query("api_key") apiKey: String,
    @Query("start_date") startDate: String,
    @Query("end_date") endDate: String
  ): List<GazeImage>
}
