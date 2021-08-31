package com.adwi.pexwallpapers.ui.preview

import androidx.lifecycle.viewModelScope
import com.adwi.pexwallpapers.data.local.entity.Wallpaper
import com.adwi.pexwallpapers.data.repository.interfaces.WallpaperRepositoryInterface
import com.adwi.pexwallpapers.shared.base.BaseViewModel
import com.adwi.pexwallpapers.util.onIO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class PreviewViewModel @Inject constructor(
    private val repository: WallpaperRepositoryInterface
) : BaseViewModel() {

    private val categoryName = MutableStateFlow("")

    val wallpaperList = categoryName.flatMapLatest { name ->
        repository.getWallpapersOfCategory(name)
    }.stateIn(viewModelScope, SharingStarted.Lazily, null)

    fun onFavoriteClick(wallpaper: Wallpaper) {
        val isFavorite = wallpaper.isFavorite
        wallpaper.isFavorite = !isFavorite
        onIO {
            repository.updateWallpaper(wallpaper)
        }
    }

    fun setCategoryName(category: String) {
        categoryName.value = category
    }
}