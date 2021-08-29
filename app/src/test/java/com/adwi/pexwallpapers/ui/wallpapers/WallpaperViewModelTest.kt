package com.adwi.pexwallpapers.ui.wallpapers

import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.adwi.pexwallpapers.mock.WallpapersMock
import com.adwi.pexwallpapers.repository.FakeFavoritesRepository
import com.adwi.pexwallpapers.repository.FakeSettingsRepository
import com.adwi.pexwallpapers.repository.FakeWallpaperRepository
import com.adwi.pexwallpapers.util.TestCoroutineRule
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ObsoleteCoroutinesApi
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
    val testCoroutineRule = TestCoroutineRule()

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
        testCoroutineRule.testDispatcher.runBlockingTest {
            repository.deleteAllWallpaper()
        }
    }

    @Test
    fun `onFavoriteClick changes isFavorite returns true`() {
        testCoroutineRule.testDispatcher.runBlockingTest {
            repository.insert(wallpaper1)
            viewModel.onFavoriteClick(wallpaper1)
            assertThat(wallpaper1.isFavorite).isTrue()
        }
    }

    @Test
    fun `onManualRefresh changes triggerChannel to FORCE return true`() {
        testCoroutineRule.testDispatcher.runBlockingTest {
            viewModel.onManualRefresh()
            viewModel.refreshTrigger.test {
                assertEquals(awaitItem(), Refresh.FORCE)
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun `onStart triggerChannel NORMAL return true`() {
        testCoroutineRule.testDispatcher.runBlockingTest {
            viewModel.onStart()
            viewModel.refreshTrigger.test {
                assertEquals(awaitItem(), Refresh.NORMAL)
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

//    @Test
//    fun `repository insert wallpaper get list returns true`() {
//        runBlocking {
//            repository.insert(wallpaper1)
//            val list = mutableListOf(wallpaper1)
//            viewModel.getWallpapers(true).test {
//                assertEquals(Resource.Success(list), awaitItem())
//                cancelAndIgnoreRemainingEvents()
//            }
//        }
//    }
}