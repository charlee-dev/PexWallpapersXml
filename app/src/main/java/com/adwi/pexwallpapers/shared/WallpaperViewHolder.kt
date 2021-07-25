package com.adwi.pexwallpapers.shared

import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.adwi.pexwallpapers.R
import com.adwi.pexwallpapers.data.local.entity.Wallpaper
import com.adwi.pexwallpapers.databinding.WallpaperItemBinding
import kotlin.random.Random

class WallpaperViewHolder(
    private val binding: WallpaperItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(wallpaper: Wallpaper) {
        binding.apply {
            wallpaperImageView.load(wallpaper.src?.medium) {
                placeholder(R.drawable.placeholder_item)
                crossfade(600)
            }

            val photographer = wallpaper.photographer
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


    private fun getRandomHeight() = 200 + Random.nextInt(200, 400)

}