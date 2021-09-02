package com.adwi.pexwallpapers.data.local.dao

import androidx.test.filters.SmallTest
import com.adwi.pexwallpapers.data.local.WallpaperDatabase
import com.adwi.pexwallpapers.data.local.entity.WallpaperMockAndroid
import com.adwi.pexwallpapers.util.MainCoroutineScopeRuleAndroid
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
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
@SmallTest
class WallpaperDaoTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val coroutineScope = MainCoroutineScopeRuleAndroid()

    @Inject
    @Named("test_database")
    lateinit var database: WallpaperDatabase

    private lateinit var dao: WallpapersDao
    private val firstWallpaper = WallpaperMockAndroid.first
    private val secondWallpaper = WallpaperMockAndroid.second

    @Before
    fun setup() {
        hiltRule.inject()
        dao = database.wallpaperDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertWallpaperAndGetWallpapersTheSameWallpaperList_returnTrue() =
        coroutineScope.dispatcher.runBlockingTest {

            val expected = listOf(firstWallpaper)

            dao.insertWallpapers(expected)

            val actual = dao.getAllWallpapers().first()
            assertThat(actual).isEqualTo(expected)
        }

    @Test
    fun insertTwoWallpapersGetsOneWallpaperOfCategoryFlowers_returnsTrue() =
        coroutineScope.dispatcher.runBlockingTest {
            val list = listOf(firstWallpaper, secondWallpaper)
            val expected = listOf(firstWallpaper)

            dao.insertWallpapers(list)

            val actual = dao.getWallpapersOfCategory("Flowers").first()
            assertEquals(expected, actual)
        }

    @Test
    fun updateWallpaperFavorite_returnsTrue() = coroutineScope.dispatcher.runBlockingTest {

        dao.insertWallpapers(listOf(firstWallpaper))
        firstWallpaper.isFavorite = true
        dao.updateWallpaperFavorite(firstWallpaper)

        val actual = dao.getAllWallpapers().first()[0]
        assertEquals(firstWallpaper, actual)
    }
}