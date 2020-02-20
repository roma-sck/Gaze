package dev.sasikanth.gaze.imagesgrid

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.spotify.mobius.android.MobiusLoopViewModel
import dev.sasikanth.gaze.R
import dev.sasikanth.gaze.imagesgrid.ImagesGridAdapter.Companion.GRID_ITEM_TYPE_ERROR
import dev.sasikanth.gaze.imagesgrid.ImagesGridAdapter.Companion.GRID_ITEM_TYPE_IMAGE
import dev.sasikanth.gaze.imagesgrid.ImagesGridAdapter.Companion.GRID_ITEM_TYPE_PROGRESS
import dev.sasikanth.gaze.utils.doOnApplyWindowInsets
import kotlinx.android.synthetic.main.fragment_images_grid.*

class ImagesGridFragment : Fragment(R.layout.fragment_images_grid), ImagesGridUi {

  private val viewModel: MobiusLoopViewModel<ImagesGridModel, ImagesGridEvent, ImagesGridEffect, ImagesGridViewEffect> by activityViewModels()
  private val gridAdapter = ImagesGridAdapter(
    retryFetchMoreImagesClicked = {
      viewModel.dispatchEvent(RetryFetchMoreImageClicked)
    }
  )

  private val adapterDataObserver = object : RecyclerView.AdapterDataObserver() {
    override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
      super.onItemRangeInserted(positionStart, itemCount)
      val viewType = gridAdapter.getItemViewType(positionStart)
      if (viewType == GRID_ITEM_TYPE_PROGRESS || viewType == GRID_ITEM_TYPE_ERROR) {
        imagesGrid.smoothScrollToPosition(positionStart)
      }
    }
  }

  private val uiRenderer by lazy {
    ImagesGridUiRenderer(this)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    viewModel.models.observe(viewLifecycleOwner, Observer { model ->
      uiRenderer.render(model)
    })

    imagesGrid.apply {

      (layoutManager as GridLayoutManager).apply {
        spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
          override fun getSpanSize(position: Int): Int {
            return when (val itemViewType = gridAdapter.getItemViewType(position)) {
              GRID_ITEM_TYPE_IMAGE -> 1
              GRID_ITEM_TYPE_PROGRESS -> 2
              GRID_ITEM_TYPE_ERROR -> 2
              else -> throw IllegalArgumentException("Unknown item view type: $itemViewType")
            }
          }
        }
      }

      adapter = gridAdapter

      addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
          super.onScrollStateChanged(recyclerView, newState)
          val lastItemPosition = gridAdapter.itemCount - 1
          val itemViewType = gridAdapter.getItemViewType(lastItemPosition)

          if (itemViewType == GRID_ITEM_TYPE_IMAGE) {
            if (!recyclerView.canScrollVertically(1)) {
              viewModel.dispatchEvent(ImagesListReachedEnd)
            }
          }
        }
      })

      doOnApplyWindowInsets { view, insets, padding ->
        view.updatePadding(top = padding.top + insets.systemWindowInsetTop, bottom = padding.bottom + insets.systemWindowInsetBottom)
      }
    }

    gridAdapter.registerAdapterDataObserver(adapterDataObserver)
  }

  override fun onDestroyView() {
    gridAdapter.unregisterAdapterDataObserver(adapterDataObserver)
    super.onDestroyView()
  }

  override fun showProgress() {
    imagesProgress.isVisible = true
    imagesGrid.isVisible = false
  }

  override fun hideProgress() {
    imagesProgress.isVisible = false
    imagesGrid.isVisible = true
  }

  override fun showImages(gridItems: List<GridItem>) {
    gridAdapter.submitList(gridItems)
    imagesGrid.isVisible = true
    imagesGridErrorTextView.isVisible = false
    imagesGridRetryButton.isVisible = false
  }

  override fun showError(error: String) {
    imagesGrid.isVisible = false
    imagesGridErrorTextView.isVisible = true
    imagesGridErrorTextView.text = error
    imagesGridRetryButton.isVisible = true
  }
}
