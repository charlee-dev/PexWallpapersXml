package com.adwi.pexwallpapers.ui.wallpapers

import app.cash.turbine.test
import com.adwi.pexwallpapers.data.local.entity.Wallpaper
import com.adwi.pexwallpapers.mock.WallpapersMock
import com.adwi.pexwallpapers.repository.FakeFavoritesRepository
import com.adwi.pexwallpapers.repository.FakeSettingsRepository
import com.adwi.pexwallpapers.repository.FakeWallpaperRepository
import com.adwi.pexwallpapers.util.CoroutineTestRule
import com.adwi.pexwallpapers.util.Resource
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Assert.assertEquals
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
class WallpaperViewModelTest {

    private lateinit var viewModel: WallpaperViewModel
    private lateinit var repository: FakeWallpaperRepository

    private val wallpaper1 = WallpapersMock.first

    @get:Rule
    val coroutineScope = CoroutineTestRule().testDispatcher

    @Before
    fun setup() {
        repository = FakeWallpaperRepository()
        viewModel = WallpaperViewModel(
            FakeWallpaperRepository(),
            FakeFavoritesRepository(),
            FakeSettingsRepository(),
            coroutineScope
        )
    }

    @After
    fun tearDown() {
        coroutineScope.runBlockingTest {
            repository.deleteAllWallpapers()
        }
    }

    @Test
    fun `onFavoriteClick changes isFavorite returns true`() {
        coroutineScope.runBlockingTest {
            wallpaper1.isFavorite = false
            viewModel.onFavoriteClick(wallpaper1)
            assertThat(wallpaper1.isFavorite).isTrue()
        }
    }

    @Test
    fun `onManualRefresh changes triggerChannel to FORCE return true`() {
        coroutineScope.runBlockingTest {
            viewModel.onManualRefresh()
            viewModel.refreshTrigger.test {
                assertEquals(Refresh.FORCE, awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun `onStart triggerChannel NORMAL return true`() {
        coroutineScope.runBlockingTest {
            viewModel.onStart()
            viewModel.refreshTrigger.test {
                assertEquals(Refresh.NORMAL, awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun `check if getWallpapers returns Resource Success returns true`() =
        coroutineScope.runBlockingTest {

            repository.insert(wallpaper1)
            val list = mutableListOf<Wallpaper>()
            list.add(wallpaper1)

//            whenever(repository.getWallpapers()).thenReturn(
//                flowOf(
//                    list.toList()
//                )
//            )

            viewModel.getWallpapers(false).test {
                assertThat(awaitItem() is Resource.Success).isTrue()
                awaitComplete()
            }
        }
}