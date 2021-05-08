package dev.sasikanth.gaze.data.source

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import dev.sasikanth.gaze.BuildConfig
import dev.sasikanth.gaze.data.APod
import dev.sasikanth.gaze.data.source.local.APodDao
import dev.sasikanth.gaze.data.source.remote.APodApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

class APodRepository
@Inject constructor(
    private val localSource: APodDao,
    private val remoteSource: APodApiService
) {

    private val aPodBoundaryCallback = APodBoundaryCallback(
        localSource = localSource,
        remoteSource = remoteSource
    )
    val networkState = aPodBoundaryCallback.networkState

    suspend fun getLatestAPod() {
        val now = LocalDate.now(ZoneId.of("America/Los_Angeles"))
        val lastLatestAPod = localSource.getLatestAPodDate()

        if (lastLatestAPod != null && now.isAfter(lastLatestAPod)) {
            try {
                val aPodResponse = withContext(Dispatchers.IO) {
                    remoteSource.getAPod(BuildConfig.API_KEY, now.toString())
                }
                if (aPodResponse.isSuccessful) {
                    val latestApod = aPodResponse.body()
                    if (latestApod != null) {
                        localSource.insertAPod(latestApod)
                    }
                }
            } catch (e: Exception) {
                Timber.e(e.localizedMessage)
            }
        }
    }

    fun getAPods(): LiveData<PagedList<APod>> {
        // PagedList config
        // While it's recommended to have prefetch distance as large as possible compared
        // to page size. In order to avoid calling API calls even before reaching end, I have
        // set prefetch distance to 10
        val pagedListConfig = PagedList.Config.Builder()
            .setPageSize(20)
            .setPrefetchDistance(10)
            .build()

        // Building LivePagedList
        return LivePagedListBuilder(localSource.getAPods(), pagedListConfig)
            .setBoundaryCallback(aPodBoundaryCallback)
            .build()
    }
}
