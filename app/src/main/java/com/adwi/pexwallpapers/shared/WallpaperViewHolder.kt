package com.adwi.pexwallpapers.shared

import androidx.recyclerview.widget.RecyclerView
import com.adwi.pexwallpapers.data.local.entity.Wallpaper
import com.adwi.pexwallpapers.databinding.WallpaperItemBinding

class WallpaperViewHolder(
    private val binding: WallpaperItemBinding,
    private val onItemClick: (Int) -> Unit,
    private val onFavoriteClick: (Int) -> Unit,
    private val onShareClick: (Int) -> Unit,
    private val onPexelsLogoClick: (Int) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(wallpaper: Wallpaper) {
        binding.wallpaper = wallpaper
        binding.executePendingBindings()
    }

    init {
        binding.apply {
            wallpaperImageView.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(position)
                }
            }
            pexelsLogo.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onPexelsLogoClick(position)
                }
            }
            favoritesBookmark.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onFavoriteClick(position)
                }
            }
            shareButton.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onShareClick(position)
                }
            }
        }
    }
}