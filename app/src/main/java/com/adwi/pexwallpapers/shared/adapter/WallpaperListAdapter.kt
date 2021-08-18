package com.adwi.pexwallpapers.shared.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.ListAdapter
import com.adwi.pexwallpapers.data.local.entity.Wallpaper
import com.adwi.pexwallpapers.databinding.LayoytWallpaperItemBinding

class WallpaperListAdapter(
    private val requireActivity: FragmentActivity,
    private val onItemClick: (Wallpaper) -> Unit
) : ListAdapter<Wallpaper, WallpaperViewHolder>(WallpaperComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WallpaperViewHolder {
        val binding =
            LayoytWallpaperItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return WallpaperViewHolder(
            binding,
            requireActivity = requireActivity,
            onItemClick = { position ->
                val wallpaper = getItem(position)
                if (wallpaper != null) {
                    onItemClick(wallpaper)
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