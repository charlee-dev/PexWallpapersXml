package com.adwi.pexwallpapers.ui.favorites

import androidx.lifecycle.viewModelScope
import com.adwi.pexwallpapers.data.local.entity.Wallpaper
import com.adwi.pexwallpapers.data.repository.interfaces.FavoritesRepositoryInterface
import com.adwi.pexwallpapers.di.IoDispatcher
import com.adwi.pexwallpapers.ui.base.BaseViewModel
import com.adwi.pexwallpapers.util.onDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val repository: FavoritesRepositoryInterface,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : BaseViewModel() {

    val favorites = getFavorites()
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    fun onDeleteAllFavorites() {
        onDispatcher(ioDispatcher) {
            repository.resetAllFavorites()
        }
    }

    fun getFavorites() = repository.getAllFavorites()

    fun onFavoriteClick(wallpaper: Wallpaper) {
        wallpaper.isFavorite = !wallpaper.isFavorite
        onDispatcher(ioDispatcher) {
            repository.updateFavorites(wallpaper)
        }
    }
}