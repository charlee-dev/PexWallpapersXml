package com.adwi.pexwallpapers.ui.preview

import androidx.lifecycle.viewModelScope
import com.adwi.pexwallpapers.data.WallpaperRepository
import com.adwi.pexwallpapers.data.local.entity.Wallpaper
import com.adwi.pexwallpapers.shared.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PreviewViewModel @Inject constructor(private val repository: WallpaperRepository) :
    BaseViewModel() {

    fun getWallpaperById(id: Int) = repository.getWallpaperById(id)
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    fun onFavoriteClick(wallpaper: Wallpaper) {
        val currentlyFavorite = wallpaper.isFavorite
        val updatedWallpaper = wallpaper.copy(isFavorite = !currentlyFavorite)
        viewModelScope.launch {
            repository.updateWallpaper(updatedWallpaper)
        }
    }

    fun favoriteOnDoubleClicked(wallpaper: Wallpaper) {
        if (!wallpaper.isFavorite) {
            val updatedWallpaper = wallpaper.copy(isFavorite = true)
            viewModelScope.launch {
                repository.updateWallpaper(updatedWallpaper)
            }
        }
    }
}