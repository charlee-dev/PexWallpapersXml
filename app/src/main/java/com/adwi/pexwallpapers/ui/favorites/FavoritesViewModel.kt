package com.adwi.pexwallpapers.ui.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adwi.pexwallpapers.data.local.entity.Wallpaper
import com.adwi.pexwallpapers.data.repository.WallpaperRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val repository: WallpaperRepository
) : ViewModel() {

    val favorites = repository.getAllFavorites()
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    fun onFavoriteClick(wallpaper: Wallpaper) {
        val currentlyFavorite = wallpaper.isFavorite
        val updatedWallpaper = wallpaper.copy(isFavorite = !currentlyFavorite)
        viewModelScope.launch {
            repository.updateWallpaperFavorite(updatedWallpaper)
        }
    }

    fun onDeleteAllFavorites() {
        viewModelScope.launch {
            repository.resetAllFavorites()
        }
    }
}