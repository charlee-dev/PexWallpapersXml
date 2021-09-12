package com.adwi.pexwallpapers

import android.content.Intent
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.adwi.pexwallpapers.databinding.ActivityMainBinding
import com.adwi.pexwallpapers.ui.base.BaseActivity
import com.ismaeldivita.chipnavigation.ChipNavigationBar
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var bottomNav: ChipNavigationBar

    private val wallpaperFragment = R.id.wallpapersFragment
    private val searchFragment = R.id.searchFragment
    private val favoritesFragment = R.id.favoritesFragment
    private val settingsFragment = R.id.settingsFragment

    private val bottomNavDirections =
        listOf(wallpaperFragment, searchFragment, favoritesFragment, settingsFragment)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setStatusBarNavigationBar()

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        setupViews()
        setupListeners()
    }

    private fun setupViews() {
        bottomNav = binding.bottomNav
        binding.apply {
            bottomNav.setItemSelected(R.id.wallpapersFragment)
            bottomNav.setOnItemSelectedListener { itemId ->
                when (itemId) {
                    wallpaperFragment -> navController.navigate(wallpaperFragment)
                    searchFragment -> navController.navigate(searchFragment)
                    favoritesFragment -> navController.navigate(favoritesFragment)
                    settingsFragment -> navController.navigate(settingsFragment)
                }
            }
        }
    }

    private fun setupListeners() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                wallpaperFragment -> bottomNav.setItemSelected(wallpaperFragment, true)
                searchFragment -> bottomNav.setItemSelected(searchFragment, true)
                favoritesFragment -> bottomNav.setItemSelected(favoritesFragment, true)
                settingsFragment -> bottomNav.setItemSelected(settingsFragment, true)
            }
        }
        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.bottomNav.isVisible = bottomNavDirections.contains(destination.id)
        }

    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        navController.handleDeepLink(intent)
    }
}