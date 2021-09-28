package com.adwi.pexwallpapers.data.local.dao

import androidx.test.filters.SmallTest
import com.adwi.pexwallpapers.data.local.WallpaperDatabase
import com.adwi.pexwallpapers.data.local.entity.WallpaperMockAndroid
import com.adwi.pexwallpapers.CoroutineAndroidTestRule
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named

@HiltAndroidTest
@ExperimentalCoroutinesApi
@SmallTest
class WallpaperDaoTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val coroutineScope = CoroutineAndroidTestRule()

    @Inject
    @Named("test_database")
    lateinit var database: WallpaperDatabase

    private lateinit var wallpaperDao: WallpapersDao
    private val firstWallpaper = WallpaperMockAndroid.first
    private val secondWallpaper = WallpaperMockAndroid.second

    @Before
    fun setup() {
        hiltRule.inject()
        wallpaperDao = database.wallpaperDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertWallpaperAndGetWallpapersTheSameWallpaperList_returnTrue() =
        coroutineScope.dispatcher.runBlockingTest {

            val expected = listOf(firstWallpaper)

            wallpaperDao.insertWallpapers(expected)

            val actual = wallpaperDao.getAllWallpapers().first()
            assertThat(actual).isEqualTo(expected)
        }

    @Test
    fun insertTwoWallpapersGetsOneWallpaperOfCategoryFlowers_returnsTrue() =
        coroutineScope.dispatcher.runBlockingTest {
            val list = listOf(firstWallpaper, secondWallpaper)
            val expected = listOf(firstWallpaper)

            wallpaperDao.insertWallpapers(list)

            val actual = wallpaperDao.getWallpapersOfCategory("Flowers").first()
            assertEquals(expected, actual)
        }

    @Test
    fun updateWallpaperFavorite_returnsTrue() = coroutineScope.dispatcher.runBlockingTest {

        wallpaperDao.insertWallpapers(listOf(firstWallpaper))
        firstWallpaper.isFavorite = true
        wallpaperDao.updateWallpaperFavorite(firstWallpaper)

        val actual = wallpaperDao.getAllWallpapers().first()[0]
        assertEquals(firstWallpaper, actual)
    }
}