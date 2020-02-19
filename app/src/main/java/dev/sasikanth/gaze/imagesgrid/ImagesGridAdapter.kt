package dev.sasikanth.gaze.imagesgrid

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.google.android.material.button.MaterialButton
import dev.sasikanth.gaze.R
import dev.sasikanth.gaze.image.GazeImage
import kotlin.random.Random

sealed class GridItem {
  abstract val key: Int
}

data class ImageGridItem(val image: GazeImage) : GridItem() {
  override val key: Int = image.date.hashCode()
}

object ProgressGridItem : GridItem() {
  override val key: Int = Random.nextInt()
}

data class ErrorGridItem(val error: String) : GridItem() {
  override val key: Int = Random.nextInt()
}

object DiffCallback : DiffUtil.ItemCallback<GridItem>() {
  override fun areItemsTheSame(oldItem: GridItem, newItem: GridItem): Boolean {
    return oldItem.key == newItem.key
  }

  @SuppressLint("DiffUtilEquals")
  override fun areContentsTheSame(oldItem: GridItem, newItem: GridItem): Boolean {
    return oldItem == newItem
  }
}

class ImagesGridAdapter(
  private val retryFetchMoreImagesClicked: () -> Unit
) : ListAdapter<GridItem, RecyclerView.ViewHolder>(DiffCallback) {

  companion object {
    const val GRID_ITEM_TYPE_IMAGE = 0
    const val GRID_ITEM_TYPE_PROGRESS = 1
    const val GRID_ITEM_TYPE_ERROR = 2
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    val inflater = LayoutInflater.from(parent.context)
    return when (viewType) {
      GRID_ITEM_TYPE_IMAGE -> {
        val view = inflater.inflate(R.layout.grid_image_item, parent, false)
        ImageViewHolder(view)
      }
      GRID_ITEM_TYPE_PROGRESS -> {
        val view = inflater.inflate(R.layout.grid_progress_item, parent, false)
        ProgressViewHolder(view)
      }
      GRID_ITEM_TYPE_ERROR -> {
        val view = inflater.inflate(R.layout.grid_error_item, parent, false)
        ErrorViewHolder(view).apply {
          retryFetchMoreImagesButton.setOnClickListener {
            retryFetchMoreImagesClicked()
          }
        }
      }
      else -> throw IllegalArgumentException("Unknown view type: $viewType")
    }
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    when (holder) {
      is ImageViewHolder -> {
        val item = getItem(position) as ImageGridItem
        holder.render(item.image)
      }
      is ErrorViewHolder -> {
        val item = getItem(position) as ErrorGridItem
        holder.render(item.error)
      }
    }
  }

  override fun getItemViewType(position: Int): Int {
    return when (getItem(position)) {
      is ImageGridItem -> GRID_ITEM_TYPE_IMAGE
      is ProgressGridItem -> GRID_ITEM_TYPE_PROGRESS
      is ErrorGridItem -> GRID_ITEM_TYPE_ERROR
    }
  }

  class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val gridImageView: ImageView = itemView.findViewById(R.id.gridImageView)

    fun render(image: GazeImage) {
      val imageUrl = image.thumbnailUrl ?: image.hdUrl
      gridImageView.load(imageUrl)
    }
  }

  class ProgressViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

  class ErrorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val errorTextView: TextView = itemView.findViewById(R.id.errorTextView)
    val retryFetchMoreImagesButton: MaterialButton = itemView.findViewById(R.id.retryFetchMoreImages)

    fun render(error: String) {
      errorTextView.text = error
    }
  }
}
