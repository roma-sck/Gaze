package dev.sasikanth.gaze.ui.pages.grid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import dev.sasikanth.gaze.databinding.FragmentPicturesGridBinding
import dev.sasikanth.gaze.ui.MainViewModel
import dev.sasikanth.gaze.ui.adapters.APodItemListener
import dev.sasikanth.gaze.ui.adapters.APodsGridAdapter

@AndroidEntryPoint
class PicturesGridFragment : Fragment() {

    val viewModel: MainViewModel by activityViewModels()

    private val gridScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager as GridLayoutManager
            val firstVisiblePosition = layoutManager.findFirstVisibleItemPosition()
            viewModel.currentPicturePosition = firstVisiblePosition
        }
    }
    private val gridAdapter = APodsGridAdapter(APodItemListener { position ->
        // Navigate to picture view
        viewModel.currentPicturePosition = position
        findNavController().navigate(PicturesGridFragmentDirections.actionShowPicture())
    })

    private lateinit var binding: FragmentPicturesGridBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPicturesGridBinding.inflate(inflater)

        viewModel.aPods.observe(viewLifecycleOwner, { pagedList ->
            gridAdapter.submitList(pagedList)
        })

        viewModel.networkState.observe(viewLifecycleOwner, { networkState ->
            gridAdapter.setNetworkState(networkState)
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apodsGrid.apply {
            setHasFixedSize(true)
            adapter = gridAdapter

            (layoutManager as GridLayoutManager).spanSizeLookup =
                object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        return if (gridAdapter.isLoadingItem(position)) 2 else 1
                    }
                }
        }

        // Scrolling to current picture position
        scrollToPosition(viewModel.currentPicturePosition)
    }

    override fun onDestroyView() {
        binding.apodsGrid.removeOnScrollListener(gridScrollListener)
        super.onDestroyView()
    }

    private fun scrollToPosition(position: Int) {
        val podsGrid = binding.apodsGrid
        val layoutManager = podsGrid.layoutManager as GridLayoutManager

        podsGrid.doOnLayout {
            val viewAtPosition =
                layoutManager.findViewByPosition(position)
                    ?: return@doOnLayout

            val isViewFullyVisible =
                layoutManager.isViewPartiallyVisible(viewAtPosition, true, false)

            if (!isViewFullyVisible) {
                podsGrid.post {
                    layoutManager.scrollToPosition(position)
                    podsGrid.addOnScrollListener(gridScrollListener)
                }
            }
        }
    }
}
