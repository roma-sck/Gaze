package dev.sasikanth.nasa.apod.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dev.sasikanth.nasa.apod.data.APod
import dev.sasikanth.nasa.apod.data.source.APodRepository
import kotlinx.coroutines.launch

class MainViewModel @AssistedInject constructor(
    private val aPodRepository: APodRepository,
    @Assisted private val handle: SavedStateHandle
) : ViewModel() {

    companion object {
        private const val KEY_CURRENT_PICTURE_POSITION = "dev.sasikanth.nasa.apod.current_position"
    }

    @AssistedFactory
    interface Factory {
        fun create(handle: SavedStateHandle): MainViewModel
    }

    val networkState = aPodRepository.networkState
    val aPods: LiveData<PagedList<APod>> = aPodRepository.getAPods()

    var currentPicturePosition: Int = 0
        get() = handle.get(KEY_CURRENT_PICTURE_POSITION) ?: 0
        set(value) {
            if (value >= 0 && value != field) {
                field = value
                handle.set(KEY_CURRENT_PICTURE_POSITION, value)
            }
        }

    init {
        viewModelScope.launch {
            aPodRepository.getLatestAPod()
        }
    }
}
