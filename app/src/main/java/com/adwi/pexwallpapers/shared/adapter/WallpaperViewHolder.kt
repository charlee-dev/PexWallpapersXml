package com.adwi.pexwallpapers.shared.adapter

import androidx.recyclerview.widget.RecyclerView
import com.adwi.pexwallpapers.data.local.entity.Wallpaper
import com.adwi.pexwallpapers.databinding.WallpaperItemBinding

class WallpaperViewHolder(
    private val binding: WallpaperItemBinding,
    private val onItemClick: (Int) -> Unit,
    private val onItemLongClick: (Int) -> Unit,
    private val itemRandomHeight: Boolean,
    private val isStaticWidth: Boolean
) : RecyclerView.ViewHolder(binding.root) {


    fun bind(wallpaper: Wallpaper) {
        binding.wallpaper = wallpaper
        binding.executePendingBindings()
    }

    init {
        binding.apply {
            randomHeight = itemRandomHeight
            staticWidth = isStaticWidth
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