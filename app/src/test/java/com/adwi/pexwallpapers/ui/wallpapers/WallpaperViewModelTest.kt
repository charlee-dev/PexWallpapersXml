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
import kotlinx.coroutines.cancel
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
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
    private val wallpaper2 = WallpapersMock.second
    private val wallpaper3 = WallpapersMock.third

    @get:Rule
    val coroutineScope = MainCoroutineScopeRule()

    @Before
    fun setup() {
        repository = FakeWallpaperRepository()
        viewModel = WallpaperViewModel(
            FakeWallpaperRepository(),
            FakeFavoritesRepository(),
            FakeSettingsRepository()
        )
    }

    @After
    fun tearDown() {
        coroutineScope.dispatcher.runBlockingTest {
            repository.deleteAllWallpaper()
        }
    }

    @Test
    fun `onFavoriteClick changes isFavorite returns true`() {
        coroutineScope.dispatcher.runBlockingTest {
            repository.insert(wallpaper1)
            viewModel.onFavoriteClick(wallpaper1)
            assertThat(wallpaper1.isFavorite).isTrue()
        }
    }

    @Test
    fun `onManualRefresh changes triggerChannel to FORCE return true`() {
        coroutineScope.dispatcher.runBlockingTest {
            viewModel.onManualRefresh()
            viewModel.refreshTrigger.test {
                assertEquals(awaitItem(), Refresh.FORCE)
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun `onStart triggerChannel NORMAL return true`() {
        coroutineScope.dispatcher.runBlockingTest {
            viewModel.onStart()
            viewModel.refreshTrigger.test {
                assertEquals(awaitItem(), Refresh.NORMAL)
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun `repository insert wallpaper get list returns true`() {
        coroutineScope.dispatcher.runBlockingTest {
            repository.insert(wallpaper1)
            val list = ArrayList<Wallpaper>()
            list.add(wallpaper1)

            var flowList = emptyList<Wallpaper>()

            viewModel.getWallpapers(true, {}, {}).onEach {
                if (it is Resource.Success) currentCoroutineContext().cancel()
//                flowList += it.data[0]
            }
        }
    }
}