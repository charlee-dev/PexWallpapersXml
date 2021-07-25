package com.adwi.pexwallpapers.shared

import androidx.recyclerview.widget.DiffUtil
import com.adwi.pexwallpapers.data.local.entity.Wallpaper

class WallpaperComparator : DiffUtil.ItemCallback<Wallpaper>() {
    override fun areItemsTheSame(oldItem: Wallpaper, newItem: Wallpaper) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Wallpaper, newItem: Wallpaper) =
        oldItem == newItem
}