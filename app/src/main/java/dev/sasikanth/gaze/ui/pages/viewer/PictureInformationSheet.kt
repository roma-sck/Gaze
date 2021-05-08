package dev.sasikanth.gaze.ui.pages.viewer

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dev.sasikanth.gaze.databinding.PictureInformationSheetBinding
import dev.sasikanth.gaze.di.misc.injector
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class PictureInformationSheet : BottomSheetDialogFragment() {

    @Inject
    lateinit var dateFormatter: DateTimeFormatter

    private val args: PictureInformationSheetArgs by navArgs()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.setOnShowListener { shownDialog ->
            val d = shownDialog as BottomSheetDialog
            val bottomSheet =
                d.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout?
            BottomSheetBehavior.from(bottomSheet!!).state = BottomSheetBehavior.STATE_EXPANDED
        }
        return dialog
    }

    override fun onAttach(context: Context) {
        requireActivity().injector.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = PictureInformationSheetBinding.inflate(inflater).apply {
            dateFormatter = this@PictureInformationSheet.dateFormatter
        }
        binding.aPod = args.aPod

        binding.closePictureInformation.setOnClickListener {
            dismiss()
        }
        return binding.root
    }
}
