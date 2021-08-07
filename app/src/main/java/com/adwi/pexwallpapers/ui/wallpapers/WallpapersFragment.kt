package com.adwi.pexwallpapers.ui.wallpapers

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.adwi.pexwallpapers.R
import com.adwi.pexwallpapers.databinding.FragmentWallpapersBinding
import com.adwi.pexwallpapers.shared.WallpaperListAdapter
import com.adwi.pexwallpapers.shared.base.BaseFragment
import com.adwi.pexwallpapers.ui.TAG_PREVIEW_FRAGMENT
import com.adwi.pexwallpapers.ui.preview.PreviewFragment
import com.adwi.pexwallpapers.util.*
import com.adwi.pexwallpapers.util.Constants.Companion.WALLPAPER_ID
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class WallpapersFragment :
    BaseFragment<FragmentWallpapersBinding>(FragmentWallpapersBinding::inflate, false) {

    override val viewModel: WallpaperViewModel by viewModels()

    override fun setupViews() {
        val wallpaperListAdapter = WallpaperListAdapter(
            onItemClick = { wallpaper ->
                val fragmentManager = parentFragmentManager.beginTransaction()
                val previewFragment = PreviewFragment()
                val arguments = Bundle()
                arguments.putInt(WALLPAPER_ID, wallpaper.id)

                previewFragment.arguments = arguments
                fragmentManager.replace(
                    R.id.fragmentContainerView,
                    previewFragment,
                    TAG_PREVIEW_FRAGMENT
                )
                    .addToBackStack(TAG_PREVIEW_FRAGMENT)
                    .commit()
            },
            onShareClick = { wallpaper ->
                wallpaper.url?.let {
                    ShareUtil.shareWallpaper(
                        requireActivity(),
                        it
                    )
                }
            },
            onDownloadClick = { TODO() },
            onFavoriteClick = { wallpaper ->
                viewModel.onFavoriteClick(wallpaper)
            },
            onPexelLogoClick = { wallpaper ->
                val uri = Uri.parse(wallpaper.url)
                val intent = Intent(Intent.ACTION_VIEW, uri)
                requireActivity().startActivity(intent)
            }
        )

        binding.apply {
            recyclerView.apply {
                adapter = wallpaperListAdapter
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
                // hide item strange animation even when favorite clicked
                itemAnimator = null
                itemAnimator?.changeDuration = 0
            }

            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                viewModel.wallpaperList.collect {
                    val result = it ?: return@collect

                    swipeRefreshLayout.isRefreshing = result is Resource.Loading
                    recyclerView.isVisible = !result.data.isNullOrEmpty()
                    errorTextview.isVisible = result.error != null && result.data.isNullOrEmpty()
                    retryButton.isVisible = result.error != null && result.data.isNullOrEmpty()
                    errorTextview.text = getString(
                        R.string.could_not_refresh,
                        result.error?.localizedMessage
                            ?: getString(R.string.unknown_error_occurred)
                    )

                    wallpaperListAdapter.submitList(result.data) {
                        if (viewModel.pendingScrollToTopAfterRefresh) {
                            recyclerView.smoothScrollToPosition(0)
                            viewModel.pendingScrollToTopAfterRefresh = false
                        }
                    }
                }
            }

            swipeRefreshLayout.setOnRefreshListener {
                viewModel.onManualRefresh()
            }

            retryButton.setOnClickListener {
                viewModel.onManualRefresh()
            }

            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
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

        setHasOptionsMenu(true)
    }

    override fun onStart() {
        super.onStart()
        // TODO decide if its really needed
        viewModel.onStart()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_wallpapers, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            R.id.action_refresh -> {
                viewModel.onManualRefresh()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
}