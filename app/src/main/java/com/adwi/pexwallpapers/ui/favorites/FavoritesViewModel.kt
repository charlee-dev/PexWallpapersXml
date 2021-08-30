package com.adwi.pexwallpapers.ui.favorites

import androidx.lifecycle.viewModelScope
import com.adwi.pexwallpapers.data.local.entity.Wallpaper
import com.adwi.pexwallpapers.data.repository.interfaces.FavoritesRepositoryInterface
import com.adwi.pexwallpapers.shared.base.BaseViewModel
import com.adwi.pexwallpapers.util.onIO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val repository: FavoritesRepositoryInterface
) : BaseViewModel() {

    val favorites = getFavorites()
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    fun onDeleteAllFavorites() {
        onIO {
            repository.resetAllFavorites()
        }
    }

    fun getFavorites() = repository.getAllFavorites()

    fun onFavoriteClick(wallpaper: Wallpaper) {
        wallpaper.isFavorite = !wallpaper.isFavorite
        onIO {
            repository.updateFavorites(wallpaper)
        }
    }
}