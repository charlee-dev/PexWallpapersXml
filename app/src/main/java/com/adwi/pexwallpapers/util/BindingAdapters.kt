package com.adwi.pexwallpapers.util

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import coil.load
import com.adwi.pexwallpapers.R
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable

object BindingAdapters {

    @BindingAdapter("app:isFavorite")
    @JvmStatic
    fun isFavorite(imageView: ImageView, isFavorite: Boolean = false) {
        when {
            isFavorite -> {
                imageView.setImageResource(R.drawable.ic_favorite_checked)
            }
            else -> {
                imageView.setImageResource(R.drawable.ic_favorite_unchecked)
            }
        }
    }

    @BindingAdapter("app:byPhotographerText")
    @JvmStatic
    fun byPhotographerText(textView: TextView, photographerName: String) {
        val text = "by $photographerName"
        textView.text = text
    }

    @BindingAdapter("loadImageFromUrl")
    @JvmStatic
    fun loadImageFromUrl(imageView: ImageView, imageUrl: String) {
        imageView.load(imageUrl) {
            placeholder(shimmerDrawable)
            placeholder(R.drawable.placeholder_item)
            crossfade(600)
        }
        imageView.layoutParams.height = heights.random()
    }

    private val shimmer =
        Shimmer.AlphaHighlightBuilder()// The attributes for a ShimmerDrawable is set by this builder
            .setDuration(1800) // how long the shimmering animation takes to do one full sweep
            .setBaseAlpha(0.7f) //the alpha of the underlying children
            .setHighlightAlpha(0.6f) // the shimmer alpha amount
            .setDirection(Shimmer.Direction.LEFT_TO_RIGHT)
            .setAutoStart(true)
            .build()

    // This is the placeholder for the imageView
    val shimmerDrawable = ShimmerDrawable().apply {
        setShimmer(shimmer)
    }

    private val heights = listOf(830, 1220, 975, 513, 600, 790)
}