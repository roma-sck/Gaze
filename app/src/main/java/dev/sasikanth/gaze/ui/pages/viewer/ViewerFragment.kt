package dev.sasikanth.gaze.ui.pages.viewer

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import dev.sasikanth.gaze.data.APod
import dev.sasikanth.gaze.databinding.FragmentViewerBinding
import dev.sasikanth.gaze.di.misc.injector
import dev.sasikanth.gaze.di.misc.savedStateActivityViewModels
import dev.sasikanth.gaze.services.PictureDownloadService
import dev.sasikanth.gaze.ui.MainViewModel
import dev.sasikanth.gaze.ui.adapters.ViewerAdapter
import dev.sasikanth.gaze.utils.ZoomOutPageTransformer

// This can be moved into ViewModel as an single event live data
interface PictureInformationListener {
    fun showPictureInformation(aPod: APod)
    fun downloadImage(pictureName: String, downloadUrl: String?)
}

class ViewerFragment : Fragment(), PictureInformationListener {

    companion object {
        private const val STORAGE_PERMISSION_REQUEST_CODE = 1001
        private val PERMISSIONS = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }

    private val viewModel: MainViewModel by savedStateActivityViewModels { savedStateHandle ->
        injector.mainViewModel.create(savedStateHandle)
    }
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

    override fun onAttach(context: Context) {
        requireActivity().injector.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentViewerBinding.inflate(inflater).apply {
            pictureInformationListener = this@ViewerFragment
            lifecycleOwner = this@ViewerFragment
        }

        val viewerAdapter = ViewerAdapter()

        binding.apodsViewer.apply {
            adapter = viewerAdapter
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
            setPageTransformer(ZoomOutPageTransformer())
        }
        binding.exitPictureDetail.setOnClickListener {
            findNavController().navigateUp()
        }

        viewModel.aPods.observe(viewLifecycleOwner, Observer {
            viewerAdapter.submitList(it)
            binding.apodsViewer.setCurrentItem(viewModel.currentPicturePosition, false)
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apodsViewer.registerOnPageChangeCallback(pagerListener)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            STORAGE_PERMISSION_REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // Permission granted, download image
                    binding.aPod?.let {
                        downloadImage(it.title, it.hdUrl)
                    }
                } else {
                    // Permission not granted
                    Toast.makeText(
                        requireContext(),
                        "Storage permission is required to download",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun showPictureInformation(aPod: APod) {
        findNavController().navigate(ViewerFragmentDirections.actionPictureInformation(aPod))
    }

    override fun downloadImage(pictureName: String, downloadUrl: String?) {
        if (allPermissionsGranted()) {
            // Storage permission is granted, trigger download service
            PictureDownloadService.startService(requireContext(), pictureName, downloadUrl)
        } else {
            // Storage permission is not given, show dialog and ask for permission
            requestPermissions(PERMISSIONS, STORAGE_PERMISSION_REQUEST_CODE)
        }
    }

    override fun onDestroyView() {
        binding.pictureInformationListener = null
        binding.apodsViewer.unregisterOnPageChangeCallback(pagerListener)
        super.onDestroyView()
    }

    private fun allPermissionsGranted(): Boolean {
        return PERMISSIONS.all {
            ContextCompat.checkSelfPermission(
                requireContext(),
                it
            ) == PackageManager.PERMISSION_GRANTED
        }
    }
}
