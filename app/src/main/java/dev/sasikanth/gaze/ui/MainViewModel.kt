package dev.sasikanth.gaze.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.sasikanth.gaze.data.APod
import dev.sasikanth.gaze.data.source.APodRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val aPodRepository: APodRepository,
    private val handle: SavedStateHandle
) : ViewModel() {

    companion object {
        private const val KEY_CURRENT_PICTURE_POSITION = "dev.sasikanth.gaze.current_position"
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
