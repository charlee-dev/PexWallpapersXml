package com.adwi.pexwallpapers.shared.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.adwi.pexwallpapers.data.local.entity.Wallpaper
import com.adwi.pexwallpapers.databinding.WallpaperViewPagerItemBinding

class WallpaperViewPager2Adapter(
    private val onItemClick: (Wallpaper) -> Unit,
    private val onItemLongClick: (Wallpaper) -> Unit
) : ListAdapter<Wallpaper, WallpaperViewPager2ViewHolder>(WallpaperComparator()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WallpaperViewPager2ViewHolder {
        val binding =
            WallpaperViewPagerItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

        return WallpaperViewPager2ViewHolder(
            binding,
            onItemClick = { position ->
                val wallpaper = getItem(position)
                if (wallpaper != null) {
                    onItemClick(wallpaper)
                }
            },
            onItemLongClick = { position ->
                val wallpaper = getItem(position)
                if (wallpaper != null) {
                    onItemLongClick(wallpaper)
                    this.notifyItemChanged(position)
                }
            }
        )
    }

    override fun onBindViewHolder(holder: WallpaperViewPager2ViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }
}