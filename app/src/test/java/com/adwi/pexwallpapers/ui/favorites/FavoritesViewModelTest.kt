package com.adwi.pexwallpapers.ui.favorites

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.adwi.pexwallpapers.mock.WallpapersMock
import com.adwi.pexwallpapers.repository.FakeFavoritesRepository
import com.adwi.pexwallpapers.util.MainCoroutineScopeRule
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import kotlin.time.ExperimentalTime

@ObsoleteCoroutinesApi
@ExperimentalTime
@RunWith(AndroidJUnit4::class)
@Config(manifest = Config.NONE)
class FavoritesViewModelTest {

    private lateinit var viewModel: FavoritesViewModel
    private lateinit var repository: FakeFavoritesRepository

    private val wallpaper1 = WallpapersMock.first
    private val wallpaper2 = WallpapersMock.second
    private val wallpaper3 = WallpapersMock.third
    val wallpaperList = listOf(wallpaper1, wallpaper2, wallpaper3)

    @get:Rule
    val coroutineScope = MainCoroutineScopeRule()

    @Before
    fun setup() {
        repository = FakeFavoritesRepository()
        viewModel = FavoritesViewModel(FakeFavoritesRepository())
    }

    @After
    fun tearDown() {
        coroutineScope.dispatcher.runBlockingTest {
            repository.resetAllFavorites()
        }
    }

//    @Test
//    fun `add 3 wallpapers and gets 3 wallpapers returns true`() {
//        coroutineScope.dispatcher.runBlockingTest {
//
//            viewModel.onFavoriteClick(wallpaper1)
//
//            wallpaperList.forEach {
//                repository.insertWallpaper(it)
//            }
//
//            viewModel.getFavorites().test {
//                assertThat(awaitItem()).contains(wallpaper1)
//                cancelAndIgnoreRemainingEvents()
//            }
//        }
//    }
}