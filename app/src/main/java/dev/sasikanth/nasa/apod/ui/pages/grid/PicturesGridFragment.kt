package dev.sasikanth.nasa.apod.ui.pages.grid

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.sasikanth.nasa.apod.databinding.FragmentPicturesGridBinding
import dev.sasikanth.nasa.apod.di.misc.injector
import dev.sasikanth.nasa.apod.di.misc.savedStateActivityViewModels
import dev.sasikanth.nasa.apod.ui.MainViewModel
import dev.sasikanth.nasa.apod.ui.adapters.APodItemListener
import dev.sasikanth.nasa.apod.ui.adapters.APodsGridAdapter

class PicturesGridFragment : Fragment() {

    val viewModel: MainViewModel by savedStateActivityViewModels { savedStateHandle ->
        injector.mainViewModel.create(savedStateHandle)
    }
    private val gridScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val firstVisiblePosition = gridLayoutManager.findFirstVisibleItemPosition()
            viewModel.currentPicturePosition = firstVisiblePosition
        }
    }

    private lateinit var binding: FragmentPicturesGridBinding
    private lateinit var gridLayoutManager: GridLayoutManager

    override fun onAttach(context: Context) {
        requireActivity().injector.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPicturesGridBinding.inflate(inflater)

        val adapter = APodsGridAdapter(APodItemListener { position ->
            // Navigate to picture view
            viewModel.currentPicturePosition = position
            findNavController().navigate(PicturesGridFragmentDirections.actionShowPicture())
        })

        binding.apodsGrid.apply {
            setHasFixedSize(true)
            gridLayoutManager = layoutManager as GridLayoutManager
            this.adapter = adapter

            gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (adapter.getItemViewType(position) == APodsGridAdapter.LOADING_ITEM) {
                        2
                    } else {
                        1
                    }
                }
            }
        }

        viewModel.aPods.observe(viewLifecycleOwner, Observer { pagedList ->
            adapter.submitList(pagedList)
        })

        viewModel.networkState.observe(viewLifecycleOwner, Observer { networkState ->
            adapter.setNetworkState(networkState)
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Scrolling to current picture position
        scrollToPosition()
    }

    override fun onDestroyView() {
        binding.apodsGrid.removeOnScrollListener(gridScrollListener)
        super.onDestroyView()
    }

    private fun scrollToPosition() {
        binding.apodsGrid.apply {
            doOnLayout {
                val viewAtPosition =
                    gridLayoutManager.findViewByPosition(viewModel.currentPicturePosition)
                if (viewAtPosition == null ||
                    gridLayoutManager.isViewPartiallyVisible(viewAtPosition, false, true)
                ) {
                    post {
                        gridLayoutManager.scrollToPosition(viewModel.currentPicturePosition)
                        addOnScrollListener(gridScrollListener)
                    }
                }
            }
        }
    }
}
