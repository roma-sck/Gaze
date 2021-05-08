package dev.sasikanth.gaze.data.source

import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import dev.sasikanth.gaze.BuildConfig
import dev.sasikanth.gaze.data.APod
import dev.sasikanth.gaze.data.NetworkState
import dev.sasikanth.gaze.data.source.local.APodDao
import dev.sasikanth.gaze.data.source.remote.APodApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.Month

class APodBoundaryCallback(
    private val localSource: APodDao,
    private val remoteSource: APodApiService
) : PagedList.BoundaryCallback<APod>() {

    val networkState = MutableLiveData<NetworkState>()

    private val uiScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    private var isLoading = false

    // Limiting data to 16 Jun 1995, since API only has data from that date onwards
    private val limitDate = LocalDate.of(1995, Month.JUNE, 16)

    override fun onZeroItemsLoaded() {
        super.onZeroItemsLoaded()
        loadInitialPods()
    }

    override fun onItemAtEndLoaded(itemAtEnd: APod) {
        super.onItemAtEndLoaded(itemAtEnd)
        loadPodsUntil(itemAtEnd.date)
    }

    private fun loadPodsUntil(endDate: LocalDate) {
        if (isLoading) return

        uiScope.launch {
            isLoading = true

            val startDate = endDate.minusDays(20)

            loadAndSavePods(startDate, endDate)

            isLoading = false
        }
    }

    private fun loadInitialPods() {
        if (isLoading) return

        uiScope.launch {
            isLoading = true

            val endDate = LocalDate.now()
            val startDate = endDate.minusDays(20)

            loadAndSavePods(startDate, endDate)

            isLoading = false
        }
    }

    private suspend fun loadAndSavePods(
        startDate: LocalDate,
        endDate: LocalDate
    ) {
        if (startDate >= limitDate) {
            networkState.postValue(NetworkState.Loading)
            try {
                // Getting images and filtering them so that only image type are saved into db
                val picturesResponse = withContext(Dispatchers.IO) {
                    remoteSource.getAPods(
                        BuildConfig.API_KEY,
                        startDate.toString(),
                        endDate.toString()
                    )
                }
                if (picturesResponse.isSuccessful) {
                    localSource.insertAPod(
                        *picturesResponse.body().orEmpty().toTypedArray()
                    )
                    networkState.postValue(NetworkState.Success)
                } else {
                    when (picturesResponse.code()) {
                        400 -> {
                            networkState.postValue(NetworkState.BadRequestError)
                        }
                        404 -> {
                            networkState.postValue(NetworkState.NotFoundError)
                        }
                        500 -> {
                            networkState.postValue(NetworkState.ServerError)
                        }
                        else -> {
                            networkState.postValue(
                                NetworkState.UnknownError(picturesResponse.code())
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                networkState.postValue(NetworkState.Exception(e.localizedMessage))
            }
        } else {
            networkState.postValue(NetworkState.Success)
        }
    }
}
