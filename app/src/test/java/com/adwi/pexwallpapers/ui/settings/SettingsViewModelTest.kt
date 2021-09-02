//package com.adwi.pexwallpapers.ui.settings
//
//import androidx.test.ext.junit.runners.AndroidJUnit4
//import app.cash.turbine.test
//import com.adwi.pexwallpapers.R
//import com.adwi.pexwallpapers.mock.SettingsMock
//import com.adwi.pexwallpapers.repository.FakeSettingsRepository
//import com.adwi.pexwallpapers.util.MainCoroutineScopeRule
//import com.google.common.truth.Truth.assertThat
//import kotlinx.coroutines.ObsoleteCoroutinesApi
//import kotlinx.coroutines.test.runBlockingTest
//import org.junit.After
//import org.junit.Before
//import org.junit.Rule
//import org.junit.Test
//import org.junit.runner.RunWith
//import org.robolectric.annotation.Config
//import kotlin.time.ExperimentalTime
//
//@ObsoleteCoroutinesApi
//@ExperimentalTime
//@RunWith(AndroidJUnit4::class)
//@Config(manifest = Config.NONE)
//class SettingsViewModelTest {
//
//    private lateinit var viewModel: SettingsViewModel
//    private lateinit var repository: FakeSettingsRepository
//
//    private val settingsMock = SettingsMock.settings
//
//    @get:Rule
//    val coroutineScope = MainCoroutineScopeRule()
//
//    @Before
//    fun setup() {
//        repository = FakeSettingsRepository()
//        viewModel = SettingsViewModel(FakeSettingsRepository())
//    }
//
//    @After
//    fun tearDown() {
//        coroutineScope.dispatcher.runBlockingTest {
//            repository.resetAllSettings()
//        }
//    }
//
//    @Test
//    fun `updatePushNotification changes pushNotification returns true`() =
//        coroutineScope.dispatcher.runBlockingTest {
//            repository.insertSettings(settingsMock)
//            viewModel.updatePushNotification(true)
//            viewModel.currentSettings.test {
//                assertThat(awaitItem().pushNotification).isTrue()
//                cancelAndIgnoreRemainingEvents()
//            }
//        }
//
//    @Test
//    fun `updateNewWallpaperSet changes newWallpaperSet returns true`() =
//        coroutineScope.dispatcher.runBlockingTest {
//            repository.insertSettings(settingsMock)
//            viewModel.updateNewWallpaperSet(true)
//            viewModel.currentSettings.test {
//                assertThat(awaitItem().newWallpaperSet).isTrue()
//                cancelAndIgnoreRemainingEvents()
//            }
//        }
//
//    @Test
//    fun `updateAutoChangeWallpaper changes autoChangeWallpaper returns true`() =
//        coroutineScope.dispatcher.runBlockingTest {
//            repository.insertSettings(settingsMock)
//            viewModel.updateAutoChangeWallpaper(true)
//            viewModel.currentSettings.test {
//                assertThat(awaitItem().autoChangeWallpaper).isTrue()
//                cancelAndIgnoreRemainingEvents()
//            }
//        }
//
//    @Test
//    fun `updateWallpaperRecommendations changes wallpaperRecommendations returns true`() =
//        coroutineScope.dispatcher.runBlockingTest {
//            repository.insertSettings(settingsMock)
//            viewModel.updateWallpaperRecommendations(true)
//            viewModel.currentSettings.test {
//                assertThat(awaitItem().wallpaperRecommendations).isTrue()
//                cancelAndIgnoreRemainingEvents()
//            }
//        }
//
//    @Test
//    fun `updateDownloadOverWiFi changes downloadOverWiFi returns true`() =
//        coroutineScope.dispatcher.runBlockingTest {
//            repository.insertSettings(settingsMock)
//            viewModel.updateDownloadOverWiFi(true)
//            viewModel.currentSettings.test {
//                assertThat(awaitItem().downloadOverWiFi).isTrue()
//                cancelAndIgnoreRemainingEvents()
//            }
//        }
//
//    @Test
//    fun `updateChangePeriodValue changes sliderValue returns true`() =
//        coroutineScope.dispatcher.runBlockingTest {
//            repository.insertSettings(settingsMock)
//            viewModel.updateChangePeriodValue(30f)
//            viewModel.currentSettings.test {
//                assertThat(awaitItem().sliderValue).isEqualTo(30f)
//                cancelAndIgnoreRemainingEvents()
//            }
//        }
//
//    @Test
//    fun `updateChangePeriodType changes selectedButton returns true`() =
//        coroutineScope.dispatcher.runBlockingTest {
//            repository.insertSettings(settingsMock)
//            viewModel.updateChangePeriodType(R.id.minutes_radio_button)
//            viewModel.currentSettings.test {
//                assertThat(awaitItem().selectedButton).isEqualTo(R.id.minutes_radio_button)
//                cancelAndIgnoreRemainingEvents()
//            }
//        }
//}