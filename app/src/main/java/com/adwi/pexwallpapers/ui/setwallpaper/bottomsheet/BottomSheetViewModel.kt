package com.adwi.pexwallpapers.ui.setwallpaper.bottomsheet

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.adwi.pexwallpapers.data.local.entity.Wallpaper
import com.adwi.pexwallpapers.data.repository.interfaces.WallpaperRepositoryInterface
import com.adwi.pexwallpapers.di.IoDispatcher
import com.adwi.pexwallpapers.shared.tools.image.ImageTools
import com.adwi.pexwallpapers.shared.tools.sharing.SharingTools
import com.adwi.pexwallpapers.ui.base.BaseViewModel
import com.adwi.pexwallpapers.util.onDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class BottomSheetViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val wallpaperRepository: WallpaperRepositoryInterface,
    private val sharingTools: SharingTools,
    private val imageTools: ImageTools,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : BaseViewModel() {

    private var savedCategoryName: String? = null
    private val categoryName = MutableStateFlow<String?>(null)

    val wallpaperResults = categoryName.flatMapLatest { categoryName ->
        categoryName?.let {
            wallpaperRepository.getWallpapersOfCategory(categoryName)
        } ?: emptyFlow()
    }.stateIn(viewModelScope, SharingStarted.Lazily, null)

    init {
        savedCategoryName = savedStateHandle.get(LAST_CATEGORY_NAME)
        if (savedCategoryName.isNullOrBlank()) {
            categoryName.value = savedCategoryName
        }
    }

    fun onCategoryNameSubmit(categoryName: String) {
        this.categoryName.value = categoryName
        savedStateHandle.set(LAST_CATEGORY_NAME, categoryName)
    }

    fun onFavoriteClick(wallpaper: Wallpaper) {
        val isFavorite = wallpaper.isFavorite
        wallpaper.isFavorite = !isFavorite
        onDispatcher(ioDispatcher) {
            wallpaperRepository.updateWallpaper(wallpaper)
        }
    }

    fun goToPexels(wallpaper: Wallpaper) {
        sharingTools.openUrlInBrowser(wallpaper.url!!)
    }

    fun shareWallpaper(wallpaper: Wallpaper) {
        onDispatcher(ioDispatcher) {
            val uri = imageTools.fetchRemoteAndSaveLocally(wallpaper.id, wallpaper.src!!.portrait)
            sharingTools.shareImage(uri, wallpaper)
        }
    }

    fun downloadWallpaper(wallpaper: Wallpaper) {
        onDispatcher(ioDispatcher) {
            imageTools.fetchRemoteAndSaveToGallery(wallpaper.id, wallpaper.src!!.portrait)
        }
    }

    companion object {
        private const val LAST_CATEGORY_NAME = "LAST_CATEGORY_NAME"
    }
}