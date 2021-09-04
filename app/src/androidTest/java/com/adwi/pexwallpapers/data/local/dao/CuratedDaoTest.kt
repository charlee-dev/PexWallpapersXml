package com.adwi.pexwallpapers.data.local.dao

import androidx.test.filters.SmallTest
import com.adwi.pexwallpapers.data.local.WallpaperDatabase
import com.adwi.pexwallpapers.data.local.entity.CuratedWallpaperMock
import com.adwi.pexwallpapers.data.local.entity.Wallpaper
import com.adwi.pexwallpapers.data.local.entity.WallpaperMockAndroid
import com.adwi.pexwallpapers.util.CoroutineAndroidTestRule
import com.google.common.truth.Truth
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named

@HiltAndroidTest
@SmallTest
class CuratedDaoTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val coroutineScope = CoroutineAndroidTestRule()

    @Inject
    @Named("test_database")
    lateinit var database: WallpaperDatabase

    private lateinit var curatedDao: CuratedDao
    private lateinit var wallpapersDao: WallpapersDao

    private val firstCurated = CuratedWallpaperMock.first

    private val firstWallpaper = WallpaperMockAndroid.first
    private val secondWallpaper = WallpaperMockAndroid.second

    @Before
    fun setup() {
        hiltRule.inject()
        curatedDao = database.curatedDao()
        wallpapersDao = database.wallpaperDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertCuratedWallpaperAndGetAllWallpapersResultOneCuratedWallpaper_returnTrue() =
        coroutineScope.dispatcher.runBlockingTest {

            val curatedList = listOf(firstCurated)

            wallpapersDao.insertWallpapers(listOf(firstWallpaper, secondWallpaper))
            curatedDao.insertCuratedWallpapers(curatedList)

            val actual = curatedDao.getAllCuratedWallpapers().first()
            val expected = listOf(firstWallpaper)
            Truth.assertThat(actual).isEqualTo(expected)
        }

    @Test
    fun deleteAllCuratedWallpapers_returnsTrue() =
        coroutineScope.dispatcher.runBlockingTest {

            val curatedList = listOf(firstCurated)

            wallpapersDao.insertWallpapers(listOf(firstWallpaper, secondWallpaper))
            curatedDao.insertCuratedWallpapers(curatedList)
            curatedDao.deleteAllCuratedWallpapers()

            val actual = curatedDao.getAllCuratedWallpapers().first()
            val expected = emptyList<Wallpaper>()
            Truth.assertThat(actual).isEqualTo(expected)
        }
}