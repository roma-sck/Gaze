package dev.sasikanth.gaze.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import dev.sasikanth.gaze.data.APod
import dev.sasikanth.gaze.data.NetworkState
import dev.sasikanth.gaze.databinding.NetworkStateItemBinding
import dev.sasikanth.gaze.databinding.PictureItemBinding
import java.util.concurrent.atomic.AtomicBoolean

typealias OnGridItemClicked = (view: View, position: Int) -> Unit
typealias OnImageLoaded = (position: Int) -> Unit

private val APOD_DIFF = object : DiffUtil.ItemCallback<APod>() {
    override fun areItemsTheSame(oldItem: APod, newItem: APod): Boolean {
        return oldItem.date == newItem.date
    }

    override fun areContentsTheSame(oldItem: APod, newItem: APod): Boolean {
        return oldItem == newItem
    }
}

class APodsGridAdapter(
    private val currentPosition: Int,
    private val onGridItemClicked: OnGridItemClicked,
    private val onImageLoaded: () -> Unit
) : PagedListAdapter<APod, RecyclerView.ViewHolder>(APOD_DIFF) {

    companion object {
        const val LOADING_ITEM = 0
        const val APOD_ITEM = 1
    }

    private val enterTransitionStarted = AtomicBoolean()
    private var networkState: NetworkState? = null

    fun isLoadingItem(position: Int) = getItemViewType(position) == LOADING_ITEM

    fun setNetworkState(networkState: NetworkState) {
        val previousState = this.networkState
        val hadExtraRow = hasExtraRow()
        this.networkState = networkState
        val hasExtraRow = hasExtraRow()
        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount())
            } else {
                notifyItemInserted(super.getItemCount())
            }
        } else if (hasExtraRow && previousState != networkState) {
            notifyItemChanged(itemCount - 1)
        }
    }

    private fun hasExtraRow() = networkState != null && networkState !is NetworkState.Success

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            LOADING_ITEM
        } else {
            APOD_ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            LOADING_ITEM -> NetworkStateItemViewHolder.from(parent)
            APOD_ITEM -> APodItemViewHolder.from(parent, onGridItemClicked, ::onImageLoaded)
            else -> throw IllegalArgumentException("Unknown view type $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is APodItemViewHolder) {
            holder.bind(getItem(position))
        } else if (holder is NetworkStateItemViewHolder) {
            holder.bind(networkState)
        }
    }

    private fun onImageLoaded(position: Int) {
        if (position != currentPosition) return
        if (enterTransitionStarted.getAndSet(true)) return

        onImageLoaded.invoke()
    }

    class APodItemViewHolder private constructor(
        private val binding: PictureItemBinding,
        private val onGridItemClicked: OnGridItemClicked,
        private val onImageLoaded: OnImageLoaded
    ) : RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun from(
                parent: ViewGroup,
                onGridItemClicked: OnGridItemClicked,
                onImageLoaded: OnImageLoaded
            ): APodItemViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = PictureItemBinding.inflate(layoutInflater, parent, false)
                return APodItemViewHolder(binding, onGridItemClicked, onImageLoaded)
            }
        }

        val imageView get() = binding.apodImage

        fun bind(aPod: APod?) {
            binding.aPod = aPod
            binding.position = adapterPosition
            binding.onGridItemClicked = ::onGridItemClicked
            binding.onImageLoaded = { onImageLoaded(adapterPosition) }
            binding.executePendingBindings()
        }

        private fun onGridItemClicked(position: Int) {
            onGridItemClicked.invoke(binding.apodImage, position)
        }
    }

    class NetworkStateItemViewHolder private constructor(
        private val binding: NetworkStateItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun from(parent: ViewGroup): NetworkStateItemViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = NetworkStateItemBinding.inflate(layoutInflater, parent, false)
                return NetworkStateItemViewHolder(binding)
            }
        }

        fun bind(networkState: NetworkState?) {
            binding.networkState = networkState
            binding.executePendingBindings()
        }
    }
}
