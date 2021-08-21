package com.adwi.pexwallpapers.ui.favorites

import androidx.lifecycle.viewModelScope
import com.adwi.pexwallpapers.data.WallpaperRepository
import com.adwi.pexwallpapers.shared.base.BaseViewModel
import com.adwi.pexwallpapers.util.onIO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val repository: WallpaperRepository
) : BaseViewModel() {

    val favorites = repository.getAllFavorites()
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    fun onDeleteAllFavorites() {
        onIO {
            repository.resetAllFavorites()
        }
    }
}