package dev.sasikanth.nasa.apod.data.source

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import dev.sasikanth.nasa.apod.BuildConfig
import dev.sasikanth.nasa.apod.data.APod
import dev.sasikanth.nasa.apod.data.source.local.APodDao
import dev.sasikanth.nasa.apod.data.source.remote.APodApiService
import dev.sasikanth.nasa.apod.utils.DateFormatter
import timber.log.Timber
import java.util.Calendar
import javax.inject.Inject

class APodRepository
@Inject constructor(
    private val localService: APodDao,
    private val remoteService: APodApiService
) {

    private val aPodBoundaryCallback = APodBoundaryCallback(
        localSource = localService,
        remoteSource = remoteService
    )
    val networkState = aPodBoundaryCallback.networkState

    suspend fun getLatestAPod() {
        val currentCal = Calendar.getInstance()
        val currentDay = currentCal.get(Calendar.DAY_OF_MONTH)

        val lastLatestAPod = localService.getLatestAPodDate()
        if (lastLatestAPod != null) {
            val lastLatestDay = Calendar.getInstance().apply {
                time = lastLatestAPod
            }.get(Calendar.DAY_OF_MONTH)

            if (currentDay > lastLatestDay) {
                // Load latest APod from API
                val currentDate = DateFormatter.formatDate(currentCal.time)
                try {
                    val latestApod = remoteService.getAPod(BuildConfig.API_KEY, currentDate)
                    localService.insertAPod(latestApod)
                } catch (e: Exception) {
                    Timber.e(e.localizedMessage)
                }
            }
        }
    }

    fun getAPods(): LiveData<PagedList<APod>> {
        // PagedList config
        val pagedListConfig = PagedList.Config.Builder()
            .setPageSize(20)
            .setPrefetchDistance(10)
            .setEnablePlaceholders(false)
            .build()

        // Building LivePagedList
        return LivePagedListBuilder(localService.getAPods(), pagedListConfig)
            .setBoundaryCallback(aPodBoundaryCallback)
            .build()
    }
}