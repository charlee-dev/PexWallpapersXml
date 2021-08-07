package com.adwi.pexwallpapers.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.adwi.pexwallpapers.R
import com.adwi.pexwallpapers.databinding.ActivityMainBinding
import com.adwi.pexwallpapers.ui.favorites.FavoritesFragment
import com.adwi.pexwallpapers.ui.preview.PreviewFragment
import com.adwi.pexwallpapers.ui.search.SearchFragment
import com.adwi.pexwallpapers.ui.wallpapers.WallpapersFragment
import com.adwi.pexwallpapers.util.slideUp
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var wallpapersFragment: WallpapersFragment
    private lateinit var searchFragment: SearchFragment
    private lateinit var favoritesFragment: FavoritesFragment
    private lateinit var previewFragment: PreviewFragment

    private val fragments: Array<Fragment>
        get() = arrayOf(
            wallpapersFragment,
            searchFragment,
            favoritesFragment,
            previewFragment
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
            is PreviewFragment -> getString(R.string.preview)
            else -> ""
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        if (savedInstanceState == null) {
            wallpapersFragment = WallpapersFragment()
            searchFragment = SearchFragment()
            favoritesFragment = FavoritesFragment()
            previewFragment = PreviewFragment()

            supportFragmentManager.beginTransaction()
                .add(R.id.fragmentContainerView, wallpapersFragment, TAG_WALLPAPERS_FRAGMENT)
                .add(R.id.fragmentContainerView, searchFragment, TAG_SEARCH_FRAGMENT)
                .add(R.id.fragmentContainerView, favoritesFragment, TAG_FAVORITES_FRAGMENT)
                .add(R.id.fragmentContainerView, previewFragment, TAG_PREVIEW_FRAGMENT)
                .commit()
        } else {
            wallpapersFragment =
                supportFragmentManager.findFragmentByTag(TAG_WALLPAPERS_FRAGMENT) as WallpapersFragment
            searchFragment =
                supportFragmentManager.findFragmentByTag(TAG_SEARCH_FRAGMENT) as SearchFragment
            favoritesFragment =
                supportFragmentManager.findFragmentByTag(TAG_FAVORITES_FRAGMENT) as FavoritesFragment
            previewFragment =
                supportFragmentManager.findFragmentByTag(TAG_PREVIEW_FRAGMENT) as PreviewFragment

            selectedIndex = savedInstanceState.getInt(KEY_SELECTED_INDEX, 0)
        }

        selectFragment(selectedFragment)

        binding.apply {
            bottomNav.slideUp()

            bottomNav.setOnItemSelectedListener { item ->
                val fragment = when (item.itemId) {
                    R.id.wallpapersFragment -> wallpapersFragment
                    R.id.searchFragment -> searchFragment
                    R.id.favoritesFragment -> favoritesFragment
                    else -> throw IllegalArgumentException("Unexpected itemId")
                }

                if (selectedFragment === fragment) {
                    if (fragment is OnBottomNavigationFragmentReselectedListener) {
                        fragment.onBottomNavigationFragmentReselected()
                    }
                } else {
                    selectFragment(fragment)
                }
                true
            }
        }
    }

    interface OnBottomNavigationFragmentReselectedListener {
        fun onBottomNavigationFragmentReselected()
    }

    override fun onBackPressed() {
        if (selectedIndex != 0) {
            if (selectedIndex == R.id.previewFragment) {
                super.onBackPressed()
                return
            }
            binding.bottomNav.selectedItemId = R.id.wallpapersFragment
        } else {
            super.onBackPressed()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_SELECTED_INDEX, selectedIndex)
    }
}

private const val TAG_WALLPAPERS_FRAGMENT = "TAG_WALLPAPERS_FRAGMENT"
private const val TAG_SEARCH_FRAGMENT = "TAG_SEARCH_FRAGMENT"
private const val TAG_FAVORITES_FRAGMENT = "TAG_FAVORITES_FRAGMENT"
const val TAG_PREVIEW_FRAGMENT = "TAG_PREVIEW_FRAGMENT"
private const val KEY_SELECTED_INDEX = "KEY_SELECTED_INDEX"