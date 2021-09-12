package com.adwi.pexwallpapers.util

import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import coil.load
import coil.request.CachePolicy
import com.adwi.pexwallpapers.R
import com.adwi.pexwallpapers.data.local.entity.Wallpaper
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

    @BindingAdapter("app:isVisible")
    @JvmStatic
    fun isVisible(view: View, isVisible: Boolean) {
        view.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    @BindingAdapter("app:isFirst")
    @JvmStatic
    fun isFirst(view: View, isFirst: Boolean) {
        view.visibility = if (!isFirst) View.VISIBLE else View.GONE
    }

    @BindingAdapter("app:isLast")
    @JvmStatic
    fun isLast(view: View, isLast: Boolean) {
        view.visibility = if (!isLast) View.VISIBLE else View.GONE
    }

    @BindingAdapter("app:byPhotographerText")
    @JvmStatic
    fun byPhotographerText(textView: TextView, photographerName: String) {
        val text = "by $photographerName"
        textView.text = text
    }

    @BindingAdapter("app:PhotographersNameText")
    @JvmStatic
    fun photographersNameText(textView: TextView, photographerName: String) {
        val text = "Photo by: $photographerName"
        textView.text = text
    }

    @BindingAdapter("loadImageFromUrlStaggered", "randomHeight", "staticWidth")
    @JvmStatic
    fun loadImageFromUrlStaggered(
        imageView: ImageView,
        wallpaper: Wallpaper,
        randomHeight: Boolean = true,
        staticWidth: Boolean = false
    ) {
        imageView.load(wallpaper.imageUrl) {
            placeholder(shimmerDrawable)
            placeholder(R.drawable.placeholder_item)
            crossfade(600)
            diskCachePolicy(CachePolicy.ENABLED)
            bitmapConfig(Bitmap.Config.ARGB_8888)
        }
        imageView.layoutParams.height = if (randomHeight) wallpaper.height else 500
        if (staticWidth) imageView.layoutParams.width = 400
    }

    @BindingAdapter("loadImageFromUrl")
    @JvmStatic
    fun loadImageFromUrl(imageView: ImageView, imageUrl: String) {
        imageView.load(imageUrl) {
            placeholder(shimmerDrawable)
            placeholder(R.drawable.placeholder_item)
            crossfade(600)
            diskCachePolicy(CachePolicy.ENABLED)
            bitmapConfig(Bitmap.Config.ARGB_8888)
        }
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
}