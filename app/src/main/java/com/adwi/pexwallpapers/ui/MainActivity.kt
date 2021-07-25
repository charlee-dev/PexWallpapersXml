package com.adwi.pexwallpapers.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.adwi.pexwallpapers.R
import com.adwi.pexwallpapers.databinding.ActivityMainBinding
import com.adwi.pexwallpapers.ui.favorites.FavoritesFragment
import com.adwi.pexwallpapers.ui.search.SearchFragment
import com.adwi.pexwallpapers.ui.wallpapers.WallpapersFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var wallpapersFragment: WallpapersFragment
    private lateinit var searchFragment: SearchFragment
    private lateinit var favoritesFragment: FavoritesFragment

    private val fragments: Array<Fragment>
        get() = arrayOf(
            wallpapersFragment,
            searchFragment,
            favoritesFragment
        )

    private var selectedIndex = 0

    private val selectedFragment get() = fragments[selectedIndex]

    private fun selectFragment(selectedFragment: Fragment) {
        var transaction = supportFragmentManager.beginTransaction()
        fragments.forEachIndexed { index, fragment ->
            if (selectedFragment == fragment) {
                transaction = transaction.attach(fragment)
                selectedIndex = index
            } else {
                transaction = transaction.detach(fragment)
            }
        }
        transaction.commit()

        title = when (selectedFragment) {
            is WallpapersFragment -> getString(R.string.wallpapers)
            is SearchFragment -> getString(R.string.search)
            is FavoritesFragment -> getString(R.string.favorites)
            else -> ""
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /**
         * If no configuration change, add fragments to fragment_container and select fragment 0
         * if there is configuration change, use fragmentManager to restore fragments,
         * restore selectedIndex and select fragment at selectedIndex
         */
        if (savedInstanceState == null) {
            wallpapersFragment = WallpapersFragment()
            searchFragment = SearchFragment()
            favoritesFragment = FavoritesFragment()

            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, wallpapersFragment, TAG_WALLPAPERS_FRAGMENT)
                .add(R.id.fragment_container, searchFragment, TAG_SEARCH_FRAGMENT)
                .add(R.id.fragment_container, favoritesFragment, TAG_FAVORITES_FRAGMENT)
                .commit()
        } else {
            wallpapersFragment =
                supportFragmentManager.findFragmentByTag(TAG_WALLPAPERS_FRAGMENT) as WallpapersFragment
            searchFragment =
                supportFragmentManager.findFragmentByTag(TAG_SEARCH_FRAGMENT) as SearchFragment
            favoritesFragment =
                supportFragmentManager.findFragmentByTag(TAG_FAVORITES_FRAGMENT) as FavoritesFragment

            selectedIndex = savedInstanceState.getInt(KEY_SELECTED_INDEX, 0)
        }

        selectFragment(selectedFragment)

        binding.bottomNav.setOnItemSelectedListener { item ->
            val fragment = when (item.itemId) {
                R.id.wallpapers_item -> wallpapersFragment
                R.id.search_item -> searchFragment
                R.id.favorites_item -> favoritesFragment
                else -> throw  IllegalArgumentException("Unexpected itemId")
            }

            selectFragment(fragment)
            true
        }
    }

    override fun onBackPressed() {
        if (selectedIndex != 0) {
            binding.bottomNav.selectedItemId = R.id.wallpapers_item
        } else {
            super.onBackPressed()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_SELECTED_INDEX, selectedIndex)
    }
}

private const val TAG_WALLPAPERS_FRAGMENT = "WALLPAPERS_FRAGMENT"
private const val TAG_SEARCH_FRAGMENT = "SEARCH_FRAGMENT"
private const val TAG_FAVORITES_FRAGMENT = "FAVORITES_FRAGMENT"
private const val KEY_SELECTED_INDEX = "KEY_SELECTED_INDEX"