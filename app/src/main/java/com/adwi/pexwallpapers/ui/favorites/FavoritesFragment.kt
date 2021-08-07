package com.adwi.pexwallpapers.ui.favorites

import android.content.Intent
import android.net.Uri
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.adwi.pexwallpapers.R
import com.adwi.pexwallpapers.databinding.FragmentFavoritesBinding
import com.adwi.pexwallpapers.shared.WallpaperListAdapter
import com.adwi.pexwallpapers.shared.base.BaseFragment
import com.adwi.pexwallpapers.util.ShareUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect


@AndroidEntryPoint
class FavoritesFragment :
    BaseFragment<FragmentFavoritesBinding>(FragmentFavoritesBinding::inflate, false) {

    override val viewModel: FavoritesViewModel by viewModels()

    override fun setupViews() {
        setHasOptionsMenu(true)

        val favoritesAdapter = WallpaperListAdapter(
            onItemClick = { wallpaper ->
                findNavController().navigate(
                    FavoritesFragmentDirections.actionFavoritesFragmentToPreviewFragment(
                        wallpaper
                    )
                )
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
                adapter = favoritesAdapter
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
            }

            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                viewModel.favorites.collect {
                    val favorites = it ?: return@collect
                    favoritesAdapter.submitList(favorites)
                    noFavoritesTextview.isVisible = favorites.isEmpty()
                    recyclerView.isVisible = favorites.isNotEmpty()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) =
        inflater.inflate(R.menu.menu_favorites, menu)


    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            R.id.action_delete_all_favorites -> {
                viewModel.onDeleteAllFavorites()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }


}