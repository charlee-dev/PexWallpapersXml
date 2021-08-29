package com.adwi.pexwallpapers.ui.wallpapers

import com.adwi.pexwallpapers.repository.FakeFavoritesRepository
import com.adwi.pexwallpapers.repository.FakeSettingsRepository
import com.adwi.pexwallpapers.repository.FakeWallpaperRepository
import org.junit.Before

class WallpaperViewModelTest {

    private lateinit var viewModel: WallpaperViewModel


    @Before
    fun setUp() {
        viewModel = WallpaperViewModel(
            FakeWallpaperRepository(),
            FakeFavoritesRepository(),
            FakeSettingsRepository()
        )
    }
}