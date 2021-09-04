package com.adwi.pexwallpapers.ui.favorites

import app.cash.turbine.test
import com.adwi.pexwallpapers.mock.WallpapersMock
import com.adwi.pexwallpapers.repository.FakeFavoritesRepository
import com.adwi.pexwallpapers.util.CoroutineTestRule
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.robolectric.annotation.Config
import kotlin.time.ExperimentalTime

@ObsoleteCoroutinesApi
@ExperimentalTime
@RunWith(JUnit4::class)
@Config(manifest = Config.NONE)
class FavoritesViewModelTest {

    private lateinit var viewModel: FavoritesViewModel
    private lateinit var repository: FakeFavoritesRepository

    private val wallpaper1 = WallpapersMock.first
    private val wallpaper2 = WallpapersMock.second
    private val wallpaper3 = WallpapersMock.third
    private val wallpaperList = listOf(wallpaper1, wallpaper2, wallpaper3)

    @get:Rule
    val coroutineScope = CoroutineTestRule()

    @Before
    fun setup() {
        repository = FakeFavoritesRepository()
        viewModel = FavoritesViewModel(FakeFavoritesRepository(), coroutineScope.testDispatcher)
    }

    @After
    fun tearDown() {
        coroutineScope.testDispatcher.runBlockingTest {
            repository.resetAllFavorites()
        }
    }

    @Test
    fun `onFavoriteClick changes isFavorite returns true`() =
        coroutineScope.testDispatcher.runBlockingTest {
            wallpaper1.isFavorite = false
            viewModel.onFavoriteClick(wallpaper1)
            assertThat(wallpaper1.isFavorite).isTrue()
        }

    @Test
    fun `getFavorites returns list of Wallpaper all isFavorite returns true`() =
        coroutineScope.testDispatcher.runBlockingTest {

            // TODO() This test fails for now, i comeback to it later

            wallpaper1.isFavorite = true
            repository.insertWallpaper(wallpaper1)
            val list = listOf(wallpaper1)

//            whenever(repository.getAllFavorites()).thenReturn(
//                flowOf(
//                    list
//                )
//            )

            viewModel.getFavorites().test {
                assertThat(awaitItem()).isEqualTo(list)
                cancelAndConsumeRemainingEvents()
            }
        }
}