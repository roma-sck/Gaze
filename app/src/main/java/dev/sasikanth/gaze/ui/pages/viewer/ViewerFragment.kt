package dev.sasikanth.gaze.ui.pages.viewer

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import dagger.hilt.android.AndroidEntryPoint
import dev.sasikanth.gaze.R
import dev.sasikanth.gaze.data.APod
import dev.sasikanth.gaze.databinding.FragmentViewerBinding
import dev.sasikanth.gaze.services.PictureDownloadService
import dev.sasikanth.gaze.ui.MainViewModel
import dev.sasikanth.gaze.ui.adapters.ViewerAdapter
import dev.sasikanth.gaze.utils.ZoomOutPageTransformer
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@AndroidEntryPoint
class ViewerFragment : Fragment() {

    @Inject
    lateinit var dateFormatter: DateTimeFormatter

    private val viewModel: MainViewModel by activityViewModels()

    private val viewerAdapter = ViewerAdapter()
    private val pagerListener = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            viewModel.currentPicturePosition = position
            val currentAPod = viewModel.aPods.value?.get(
                viewModel.currentPicturePosition
            )
            if (currentAPod != null) {
                binding.aPod = currentAPod
                binding.executePendingBindings()
            }
        }
    }

    private lateinit var binding: FragmentViewerBinding
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requestPermissionLauncher = registerForActivityResult(RequestPermission()) { isGranted ->
            if (isGranted)
                binding.aPod?.let {
                    startDownloadService(it.title, it.hdUrl)
                }
            else
                Toast.makeText(
                    requireContext(),
                    getString(R.string.storage_permission_error),
                    Toast.LENGTH_SHORT
                ).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentViewerBinding.inflate(inflater).apply {
            lifecycleOwner = viewLifecycleOwner
            dateFormatter = this@ViewerFragment.dateFormatter
            onShowPictureInfo = ::onShowPictureInfo
            onDownloadImage = ::onDownloadImage
        }

        binding.apodsViewer.apply {
            adapter = viewerAdapter
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
            setPageTransformer(ZoomOutPageTransformer())
        }
        binding.exitPictureDetail.setOnClickListener {
            findNavController().navigateUp()
        }

        viewModel.aPods.observe(viewLifecycleOwner, {
            viewerAdapter.submitList(it)
            binding.apodsViewer.setCurrentItem(viewModel.currentPicturePosition, false)
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apodsViewer.registerOnPageChangeCallback(pagerListener)
    }

    private fun onShowPictureInfo(aPod: APod) {
        findNavController().navigate(ViewerFragmentDirections.actionPictureInformation(aPod))
    }

    private fun onDownloadImage(pictureName: String, downloadUrl: String?) {
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) -> {
                startDownloadService(pictureName, downloadUrl)
            }
            else -> requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
    }

    private fun startDownloadService(pictureName: String, downloadUrl: String?) {
        PictureDownloadService.startService(requireContext(), pictureName, downloadUrl)
    }

    override fun onDestroyView() {
        binding.apodsViewer.unregisterOnPageChangeCallback(pagerListener)
        super.onDestroyView()
    }
}
