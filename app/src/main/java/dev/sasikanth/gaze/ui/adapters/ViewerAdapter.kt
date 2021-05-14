package dev.sasikanth.gaze.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import dev.sasikanth.gaze.data.APod
import dev.sasikanth.gaze.databinding.PictureDetailItemBinding
import java.util.concurrent.atomic.AtomicBoolean

private val VIEWER_DIFF = object : DiffUtil.ItemCallback<APod>() {
    override fun areItemsTheSame(oldItem: APod, newItem: APod): Boolean {
        return oldItem.date == newItem.date
    }

    override fun areContentsTheSame(oldItem: APod, newItem: APod): Boolean {
        return oldItem == newItem
    }
}

class ViewerAdapter(
    private val onImageLoaded: () -> Unit
) : PagedListAdapter<APod, ViewerAdapter.ViewerItemHolder>(VIEWER_DIFF) {

    private val enterTransitionStarted = AtomicBoolean()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewerItemHolder {
        return ViewerItemHolder.from(parent) {
            if (enterTransitionStarted.getAndSet(true)) return@from
            onImageLoaded.invoke()
        }
    }

    override fun onBindViewHolder(holder: ViewerItemHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewerItemHolder private constructor(
        private val binding: PictureDetailItemBinding,
        private val onImageLoaded: () -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun from(parent: ViewGroup, onImageLoaded: () -> Unit): ViewerItemHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = PictureDetailItemBinding.inflate(layoutInflater, parent, false)
                return ViewerItemHolder(binding, onImageLoaded)
            }
        }

        val imageView get() = binding.apodImage

        fun bind(aPod: APod?) {
            binding.aPod = aPod
            binding.onImageLoaded = onImageLoaded
            binding.executePendingBindings()
        }
    }
}
