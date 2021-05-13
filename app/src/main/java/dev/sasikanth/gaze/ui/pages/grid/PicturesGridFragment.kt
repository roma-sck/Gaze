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
import dagger.hilt.android.AndroidEntryPoint
import dev.sasikanth.gaze.databinding.FragmentPicturesGridBinding
import dev.sasikanth.gaze.ui.MainViewModel
import dev.sasikanth.gaze.ui.adapters.APodsGridAdapter

@AndroidEntryPoint
class PicturesGridFragment : Fragment() {

    private val viewModel: MainViewModel by activityViewModels()
    private val gridAdapter = APodsGridAdapter(::onGridItemClicked)

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

        scrollToPosition()
    }

    private fun scrollToPosition() {
        val podsGrid = binding.apodsGrid
        val layoutManager = podsGrid.layoutManager as GridLayoutManager

        fun isViewAtPosition(view: View) =
            layoutManager.isViewPartiallyVisible(view, true, false)

        podsGrid.doOnLayout {
            val viewAtPosition = layoutManager.findViewByPosition(viewModel.currentPicturePosition)
            if (viewAtPosition == null || isViewAtPosition(viewAtPosition)) {
                podsGrid.post { layoutManager.scrollToPosition(viewModel.currentPicturePosition) }
            }
        }
    }

    private fun onGridItemClicked(view: View, position: Int) {
        viewModel.currentPicturePosition = position
        findNavController().navigate(PicturesGridFragmentDirections.actionShowPicture())
    }
}
