package com.adwi.pexwallpapers.ui

import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import com.adwi.pexwallpapers.CoroutineAndroidTestRule
import com.adwi.pexwallpapers.R
import com.adwi.pexwallpapers.data.repository.FakeFavoritesRepository
import com.adwi.pexwallpapers.data.repository.FakeSettingsRepository
import com.adwi.pexwallpapers.data.repository.FakeWallpaperRepository
import com.adwi.pexwallpapers.launchFragmentInHiltContainer
import com.adwi.pexwallpapers.ui.wallpapers.WallpaperViewModel
import com.adwi.pexwallpapers.ui.wallpapers.WallpapersFragment
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

@MediumTest
@HiltAndroidTest
class WallpaperFragmentTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val disptcher =CoroutineAndroidTestRule().dispatcher

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun testNavigationFromWallpapersFragmentToPreview() {

        val navController = Mockito.mock(NavController::class.java)
        val viewmodel = WallpaperViewModel(
            FakeWallpaperRepository(),
            FakeFavoritesRepository(),
            FakeSettingsRepository(),
            disptcher
        )

        launchFragmentInHiltContainer<WallpapersFragment> {
            Navigation.setViewNavController(requireView(), navController)

            Espresso.onView(withId(R.id.recycler_view)).perform(
//                TODO()
            )
        }
    }
}