package com.adwi.pexwallpapers.shared

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.adwi.pexwallpapers.data.local.entity.Wallpaper
import com.adwi.pexwallpapers.databinding.WallpaperItemBinding

class WallpaperListAdapter(
    private val onItemClick: (Wallpaper) -> Unit,
    private val onShareClick: (Wallpaper) -> Unit,
    private val onFavoriteClick: (Wallpaper) -> Unit,
    private val onPexelLogoClick: (Wallpaper) -> Unit
) : ListAdapter<Wallpaper, WallpaperViewHolder>(WallpaperComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WallpaperViewHolder {
        val binding =
            WallpaperItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WallpaperViewHolder(
            binding,
            onItemClick = { position ->
                val wallpaper = getItem(position)
                if (wallpaper != null) {
                    onItemClick(wallpaper)
                }
            },
            onShareClick = { position ->
                val wallpaper = getItem(position)
                if (wallpaper != null) {
                    onShareClick(wallpaper)
                }
            },
            onFavoriteClick = { position ->
                val wallpaper = getItem(position)
                if (wallpaper != null) {
                    onFavoriteClick(wallpaper)
                }
            },
            onPexelsLogoClick = { position ->
                val wallpaper = getItem(position)
                if (wallpaper != null) {
                    onPexelLogoClick(wallpaper)
                }
            }
        )
    }

    override fun onBindViewHolder(holder: WallpaperViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }
}