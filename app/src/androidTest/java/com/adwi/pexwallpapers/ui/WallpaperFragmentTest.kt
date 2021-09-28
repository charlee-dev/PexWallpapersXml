package com.adwi.pexwallpapers.ui

import androidx.test.filters.MediumTest
import com.adwi.pexwallpapers.launchFragmentInHiltContainer
import com.adwi.pexwallpapers.ui.wallpapers.WallpapersFragment
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@MediumTest
@HiltAndroidTest
class WallpaperFragmentTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun testNavigationFromWallpapersFragmentToPreview() {

        launchFragmentInHiltContainer<WallpapersFragment> {
            
        }
    }
}