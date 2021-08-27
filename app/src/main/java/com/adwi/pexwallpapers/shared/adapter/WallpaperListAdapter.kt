package com.adwi.pexwallpapers.shared.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.adwi.pexwallpapers.data.local.entity.Wallpaper
import com.adwi.pexwallpapers.databinding.WallpaperItemBinding

class WallpaperListAdapter(
    private val onItemClick: (Wallpaper) -> Unit,
    private val onItemLongClick: (Wallpaper) -> Unit,
    private val itemRandomHeight: Boolean
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
            onItemLongClick = { position ->
                val wallpaper = getItem(position)
                if (wallpaper != null) {
                    onItemLongClick(wallpaper)
                    this.notifyItemChanged(position)
                }
            },
            itemRandomHeight = itemRandomHeight
        )
    }

    override fun onBindViewHolder(holder: WallpaperViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }
}