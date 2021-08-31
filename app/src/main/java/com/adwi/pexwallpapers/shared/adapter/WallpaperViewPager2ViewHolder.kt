package com.adwi.pexwallpapers.shared.adapter

import androidx.recyclerview.widget.RecyclerView
import com.adwi.pexwallpapers.data.local.entity.Wallpaper
import com.adwi.pexwallpapers.databinding.WallpaperViewPagerItemBinding

class WallpaperViewPager2ViewHolder(
    private val binding: WallpaperViewPagerItemBinding,
    private val onItemClick: (Int) -> Unit,
    private val onItemLongClick: (Int) -> Unit
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
            wallpaperImageView.setOnLongClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemLongClick(position)
                }
                true
            }
        }
    }
}