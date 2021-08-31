package com.adwi.pexwallpapers.ui.wallpapers

import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.adwi.pexwallpapers.data.local.entity.Wallpaper
import com.adwi.pexwallpapers.mock.WallpapersMock
import com.adwi.pexwallpapers.repository.FakeFavoritesRepository
import com.adwi.pexwallpapers.repository.FakeSettingsRepository
import com.adwi.pexwallpapers.repository.FakeWallpaperRepository
import com.adwi.pexwallpapers.util.MainCoroutineScopeRule
import com.adwi.pexwallpapers.util.Resource
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.robolectric.annotation.Config
import kotlin.time.ExperimentalTime

@ObsoleteCoroutinesApi
@ExperimentalTime
@RunWith(AndroidJUnit4::class)
@Config(manifest = Config.NONE)
class WallpaperViewModelTest {

    private lateinit var viewModel: WallpaperViewModel
    private lateinit var repository: FakeWallpaperRepository

    private val wallpaper1 = WallpapersMock.first


    @get:Rule
    val coroutineScope = MainCoroutineScopeRule()

    @Before
    fun setup() {
        repository = mock()
        viewModel = WallpaperViewModel(
            FakeWallpaperRepository(),
            FakeFavoritesRepository(),
            FakeSettingsRepository()
        )
    }

    @After
    fun tearDown() {
        coroutineScope.dispatcher.runBlockingTest {
            repository.deleteAllWallpapers()
        }
    }

    @Test
    fun `onFavoriteClick changes isFavorite returns true`() {
        coroutineScope.dispatcher.runBlockingTest {
            wallpaper1.isFavorite = false
            viewModel.onFavoriteClick(wallpaper1)
            assertThat(wallpaper1.isFavorite).isTrue()
        }
    }

    @Test
    fun `onManualRefresh changes triggerChannel to FORCE return true`() {
        coroutineScope.dispatcher.runBlockingTest {
            viewModel.onManualRefresh()
            viewModel.refreshTrigger.test {
                assertEquals(Refresh.FORCE, awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun `onStart triggerChannel NORMAL return true`() {
        coroutineScope.dispatcher.runBlockingTest {
            viewModel.onStart()
            viewModel.refreshTrigger.test {
                assertEquals(Refresh.NORMAL, awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun `check if getWallpapers returns Resource Success returns true`() =
        coroutineScope.dispatcher.runBlockingTest {

            repository.insert(wallpaper1)
            val list = mutableListOf<Wallpaper>()
            list.add(wallpaper1)

            whenever(repository.getWallpapers()).thenReturn(
                flowOf(
                    list.toList()
                )
            )

            viewModel.getWallpapers(false).test {
                assertThat(awaitItem() is Resource.Success).isTrue()
                awaitComplete()
            }
        }
}