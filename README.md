# Gaze - Astronomy Picture of the Day

[![Android CI](https://github.com/msasikanth/Gaze/actions/workflows/android.yml/badge.svg?branch=main)](https://github.com/msasikanth/Gaze/actions/workflows/android.yml)

Android app to display astronomy picture of the day provided by NASA 
open API (https://api.nasa.gov/api.html#apod)

Inorder to build and the app and make API requests place apiKey="key" in local.properties. 
You can create an API key here (https://api.nasa.gov/index.html#apply-for-an-api-key)

## Preview
![Preview](/images/gaze_preview.png)
 
## Libraries used
- Kotlin
- Coroutines
- Android Architecture Components (ViewModel, LiveData, Room, Paging, Navigation)
- Retrofit
- Coil
- Dagger
- Timber
- PhotoView

## TODO
- ~~Shared element transition between grid and detail pager~~
- Replace interface callbacks with ViewModel LiveData Event for click events
- Additional tests
