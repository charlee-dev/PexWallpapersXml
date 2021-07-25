package com.adwi.pexwallpapers.shared

import androidx.recyclerview.widget.RecyclerView
import com.adwi.pexwallpapers.R
import com.adwi.pexwallpapers.data.local.entity.Wallpaper
import com.adwi.pexwallpapers.databinding.WallpaperItemBinding
import com.bumptech.glide.Glide

class WallpaperViewHolder(
    private val binding: WallpaperItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(wallpaper: Wallpaper) {
        binding.apply {
            Glide.with(itemView)
                .load(wallpaper.src.tiny)
                .error(R.drawable.image_placeholder)
                .into(wallpaperImageView)

            val photographer = wallpaper.photographer ?: "pexels.com"
            val byPhotographer = "by $photographer"
            wallpaperPhotographer.text = byPhotographer

            favoritesBookmark.setImageResource(
                when {
                    wallpaper.isFavorite -> R.drawable.ic_favorite_checked
                    else -> R.drawable.ic_favorite_unchecked
                }
            )
        }
    }
}