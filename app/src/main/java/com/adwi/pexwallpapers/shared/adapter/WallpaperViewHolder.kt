package com.adwi.pexwallpapers.shared.adapter

import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.adwi.pexwallpapers.R
import com.adwi.pexwallpapers.data.local.entity.Wallpaper
import com.adwi.pexwallpapers.databinding.WallpaperItemBinding

class WallpaperViewHolder(
    private val binding: WallpaperItemBinding,
    private val onItemClick: (Int) -> Unit,
    private val onFavoriteClick: (Int) -> Unit,
    private val onShareClick: (Int) -> Unit,
    private val onPexelsLogoClick: (Int) -> Unit,
    private val requireActivity: FragmentActivity
) : RecyclerView.ViewHolder(binding.root), ActionMode.Callback {

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
                Toast.makeText(
                    requireActivity,
                    "Wallpaper: ${wallpaper!!.photographer}",
                    Toast.LENGTH_SHORT
                ).show()
                requireActivity.startActionMode(this@WallpaperViewHolder)
                true

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

    override fun onCreateActionMode(actionMode: ActionMode?, menu: Menu?): Boolean {
        actionMode?.menuInflater?.inflate(R.menu.favorites_contextual_menu, menu)
        applyStatusBarColor(R.color.contextualStatusBarColor)
        return true
    }

    override fun onPrepareActionMode(actionMode: ActionMode?, menu: Menu?): Boolean {
        return true
    }

    override fun onActionItemClicked(actionMode: ActionMode?, menu: MenuItem?): Boolean {
        return true
    }

    override fun onDestroyActionMode(actionMode: ActionMode?) {
        applyStatusBarColor(R.color.statusBarColor)
    }

    private fun applyStatusBarColor(color: Int) {
        requireActivity.window.statusBarColor =
            ContextCompat.getColor(requireActivity, color)
    }
}