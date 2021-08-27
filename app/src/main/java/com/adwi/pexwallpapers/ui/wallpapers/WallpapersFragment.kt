package com.adwi.pexwallpapers.ui.wallpapers

import android.view.MenuItem
import android.widget.*
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.adwi.pexwallpapers.R
import com.adwi.pexwallpapers.databinding.FragmentWallpapersBinding
import com.adwi.pexwallpapers.shared.adapter.WallpaperListAdapter
import com.adwi.pexwallpapers.shared.base.BaseFragment
import com.adwi.pexwallpapers.util.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class WallpapersFragment :
    BaseFragment<FragmentWallpapersBinding, WallpaperListAdapter>(
        inflate = FragmentWallpapersBinding::inflate
    ) {

    override val viewModel: WallpaperViewModel by viewModels()

    override fun setupToolbar() {
        binding.toolbarLayout.apply {
            titleTextView.text = getString(R.string.wallpapers)
            backButton.isVisible = false
        }
    }

    override fun setupAdapters() {
        mAdapter = WallpaperListAdapter(
            onItemClick = { wallpaper ->
                findNavController().navigate(
                    WallpapersFragmentDirections.actionWallpapersFragmentToPreviewFragment(
                        wallpaper
                    )
                )
            },
            onItemLongClick = { wallpaper ->
                viewModel.onFavoriteClick(wallpaper)
            },
            itemRandomHeight = true,
        )
    }

    override fun setupViews() {
        setHasOptionsMenu(true)
        binding.apply {
            shimmerFrameLayout.startShimmer()
            recyclerView.apply {
                adapter = mAdapter
                layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                setHasFixedSize(true)
                // hide item strange animation even when favorite clicked
                itemAnimator = null
                itemAnimator?.changeDuration = 0
            }
        }
    }

    override fun setupFlows() {
        binding.apply {
            launchCoroutine {
                viewModel.wallpaperList.collect {
                    val result = it ?: return@collect
                    swipeRefreshLayout.isRefreshing = result is Resource.Loading
                    shimmerFrameLayout.apply {
                        if (result.data.isNullOrEmpty()) startShimmer() else stopShimmer()
                        isVisible = result.data.isNullOrEmpty()
                    }
                    recyclerView.isVisible = !result.data.isNullOrEmpty()
                    errorTextview.isVisible = result.error != null && result.data.isNullOrEmpty()
                    retryButton.isVisible = result.error != null && result.data.isNullOrEmpty()
                    errorTextview.text = getString(
                        R.string.could_not_refresh,
                        result.error?.localizedMessage
                            ?: getString(R.string.unknown_error_occurred)
                    )

                    mAdapter!!.submitList(result.data) {
                        if (viewModel.pendingScrollToTopAfterRefresh) {
                            recyclerView.smoothScrollToPosition(0)
                            viewModel.pendingScrollToTopAfterRefresh = false
                        }
                    }
                }
            }

            launchCoroutine {
                viewModel.events.collect { event ->
                    when (event) {
                        is WallpaperViewModel.Event.ShowErrorMessage -> showSnackbar(
                            getString(
                                R.string.could_not_refresh,
                                event.error.localizedMessage
                                    ?: getString(R.string.unknown_error_occurred)
                            )
                        )
                    }.exhaustive
                }
            }
        }
    }

    override fun setupListeners() {
        binding.apply {
            swipeRefreshLayout.setOnRefreshListener {
                viewModel.onManualRefresh()
            }
            retryButton.setOnClickListener {
                viewModel.onManualRefresh()
            }
            toolbarLayout.menuButton.setOnClickListener {
                showMenu(it, R.menu.menu_wallpapers)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.onStart()
    }

    override fun onResume() {
        super.onResume()
        binding.apply {
            shimmerFrameLayout.startShimmer()
        }
    }

    override fun onPause() {
        super.onPause()
        binding.apply {
            shimmerFrameLayout.stopShimmer()
        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.action_refresh -> {
                viewModel.onManualRefresh()
                true
            }
            R.id.settingsFragment -> {
                findNavController().navigate(R.id.settingsFragment)
                true
            }
            else -> false
        }
    }
}