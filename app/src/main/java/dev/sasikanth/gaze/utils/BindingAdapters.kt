package dev.sasikanth.gaze.utils

import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.databinding.BindingAdapter
import coil.load
import com.google.android.material.card.MaterialCardView
import dev.sasikanth.gaze.R
import dev.sasikanth.gaze.data.NetworkState
import dev.sasikanth.gaze.utils.extensions.dpToPx
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@BindingAdapter("isVisibleOn")
fun View.isVisibleOn(isVisible: Boolean) {
    this.isVisible = isVisible
}

@BindingAdapter("aPodFormattedDate", "dateFormatter")
fun AppCompatTextView.setFormattedDate(date: LocalDate?, dateFormatter: DateTimeFormatter) {
    date?.let {
        text = dateFormatter.format(it)
    }
}

@BindingAdapter("imageUrl", "onImageLoaded")
fun ImageView.setImageUrl(imageUrl: String?, onImageLoaded: () -> Unit) {
    imageUrl?.let {
        load(it) {
            crossfade(true)
            placeholder(R.drawable.ic_image_loading)
            error(R.drawable.ic_image_error)
            listener(
                onSuccess = { _, _ -> onImageLoaded() },
                onError = { _, _ -> onImageLoaded() }
            )
        }
    }
}

@BindingAdapter("networkStateCard")
fun MaterialCardView.setNetworkState(networkState: NetworkState?) {
    if (networkState is NetworkState.Loading) {
        cardElevation = 0f
        setCardBackgroundColor(Color.TRANSPARENT)
    } else {
        cardElevation = 24.dpToPx().toFloat()
        setCardBackgroundColor(Color.WHITE)
    }
}

@BindingAdapter("networkStateProgress")
fun ProgressBar.setNetworkState(networkState: NetworkState?) {
    when (networkState) {
        is NetworkState.BadRequestError,
        is NetworkState.NotFoundError,
        is NetworkState.ServerError,
        is NetworkState.UnknownError,
        is NetworkState.Exception,
        is NetworkState.Success,
        null -> {
            this.isVisible = false
        }
        is NetworkState.Loading -> {
            this.isVisible = true
        }
    }
}

@BindingAdapter("networkStateError")
fun AppCompatTextView.setNetworkState(networkState: NetworkState?) {
    when (networkState) {
        is NetworkState.UnknownError -> {
            isVisible = true
            text = context.getString(
                R.string.error_unknown,
                networkState.errorCode
            )
        }
        is NetworkState.BadRequestError -> {
            isVisible = true
            setText(R.string.error_bad_format)
        }
        is NetworkState.NotFoundError -> {
            isVisible = true
            setText(R.string.error_not_found)
        }
        is NetworkState.ServerError -> {
            isVisible = true
            setText(R.string.error_server)
        }
        is NetworkState.Exception -> {
            isVisible = true
            text = networkState.message
                ?: context.getString(R.string.exception_unknown)
        }
        else -> {
            isVisible = false
        }
    }
}

@BindingAdapter("layoutFullscreen")
fun View.bindLayoutFullscreen(previousFullscreen: Boolean, fullscreen: Boolean) {
    if (previousFullscreen != fullscreen && fullscreen) {
        systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
    }
}

@BindingAdapter(
    "paddingLeftSystemWindowInsets",
    "paddingTopSystemWindowInsets",
    "paddingRightSystemWindowInsets",
    "paddingBottomSystemWindowInsets",
    requireAll = false
)
fun View.applySystemWindowInsetsPadding(
    previousApplyLeft: Boolean,
    previousApplyTop: Boolean,
    previousApplyRight: Boolean,
    previousApplyBottom: Boolean,
    applyLeft: Boolean,
    applyTop: Boolean,
    applyRight: Boolean,
    applyBottom: Boolean
) {
    if (previousApplyLeft == applyLeft &&
        previousApplyTop == applyTop &&
        previousApplyRight == applyRight &&
        previousApplyBottom == applyBottom
    ) {
        return
    }

    doOnApplyWindowInsets { view, insets, padding, _ ->
        val left = if (applyLeft) insets.systemWindowInsetLeft else 0
        val top = if (applyTop) insets.systemWindowInsetTop else 0
        val right = if (applyRight) insets.systemWindowInsetRight else 0
        val bottom = if (applyBottom) insets.systemWindowInsetBottom else 0

        view.setPadding(
            padding.left + left,
            padding.top + top,
            padding.right + right,
            padding.bottom + bottom
        )
    }
}

@BindingAdapter(
    "marginLeftSystemWindowInsets",
    "marginTopSystemWindowInsets",
    "marginRightSystemWindowInsets",
    "marginBottomSystemWindowInsets",
    requireAll = false
)
fun View.applySystemWindowInsetsMargin(
    previousApplyLeft: Boolean,
    previousApplyTop: Boolean,
    previousApplyRight: Boolean,
    previousApplyBottom: Boolean,
    applyLeft: Boolean,
    applyTop: Boolean,
    applyRight: Boolean,
    applyBottom: Boolean
) {
    if (previousApplyLeft == applyLeft &&
        previousApplyTop == applyTop &&
        previousApplyRight == applyRight &&
        previousApplyBottom == applyBottom
    ) {
        return
    }

    doOnApplyWindowInsets { view, insets, _, margin ->
        val left = if (applyLeft) insets.systemWindowInsetLeft else 0
        val top = if (applyTop) insets.systemWindowInsetTop else 0
        val right = if (applyRight) insets.systemWindowInsetRight else 0
        val bottom = if (applyBottom) insets.systemWindowInsetBottom else 0

        view.updateLayoutParams<ViewGroup.MarginLayoutParams> {
            leftMargin = margin.left + left
            topMargin = margin.top + top
            rightMargin = margin.right + right
            bottomMargin = margin.bottom + bottom
        }
    }
}

fun View.doOnApplyWindowInsets(block: (View, WindowInsets, InitialPadding, InitialMargin) -> Unit) {
    // Create a snapshot of the view's padding & margin states
    val initialPadding = recordInitialPaddingForView(this)
    val initialMargin = recordInitialMarginForView(this)
    // Set an actual OnApplyWindowInsetsListener which proxies to the given
    // lambda, also passing in the original padding & margin states
    setOnApplyWindowInsetsListener { v, insets ->
        block(v, insets, initialPadding, initialMargin)
        // Always return the insets, so that children can also use them
        insets
    }
    // request some insets
    requestApplyInsetsWhenAttached()
}

class InitialPadding(val left: Int, val top: Int, val right: Int, val bottom: Int)

class InitialMargin(val left: Int, val top: Int, val right: Int, val bottom: Int)

private fun recordInitialPaddingForView(view: View) = InitialPadding(
    view.paddingLeft, view.paddingTop, view.paddingRight, view.paddingBottom
)

private fun recordInitialMarginForView(view: View): InitialMargin {
    val lp = view.layoutParams as? ViewGroup.MarginLayoutParams
        ?: throw IllegalArgumentException("Invalid view layout params")
    return InitialMargin(lp.leftMargin, lp.topMargin, lp.rightMargin, lp.bottomMargin)
}

fun View.requestApplyInsetsWhenAttached() {
    if (isAttachedToWindow) {
        // We're already attached, just request as normal
        requestApplyInsets()
    } else {
        // We're not attached to the hierarchy, add a listener to
        // request when we are
        addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {
                v.removeOnAttachStateChangeListener(this)
                v.requestApplyInsets()
            }

            override fun onViewDetachedFromWindow(v: View) = Unit
        })
    }
}
