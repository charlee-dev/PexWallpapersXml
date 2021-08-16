package com.adwi.pexwallpapers.ui.favorites

import android.content.Intent
import android.net.Uri
import android.view.*
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.adwi.pexwallpapers.R
import com.adwi.pexwallpapers.databinding.FragmentFavoritesBinding
import com.adwi.pexwallpapers.shared.adapter.WallpaperListAdapter
import com.adwi.pexwallpapers.shared.base.BaseFragment
import com.adwi.pexwallpapers.shared.tools.SharingTools
import com.adwi.pexwallpapers.util.launchCoroutine
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect


@AndroidEntryPoint
class FavoritesFragment :
    BaseFragment<FragmentFavoritesBinding, WallpaperListAdapter>(
        inflate = FragmentFavoritesBinding::inflate,
        hasNavigation = true
    ) {

    override val viewModel: FavoritesViewModel by viewModels()

    override fun setupToolbar() {
        binding.toolbarLayout.apply {
            titleTextView.text = getString(R.string.favorites)
        }
    }

    override fun setupAdapters() {
        mAdapter = WallpaperListAdapter(
            onItemClick = { wallpaper ->
                findNavController().navigate(
                    FavoritesFragmentDirections.actionFavoritesFragmentToPreviewFragment(
                        wallpaper
                    )
                )
            },
            onShareClick = { wallpaper ->
                wallpaper.url?.let {
                    SharingTools(requireContext()).share(it)
                }
            },
            onFavoriteClick = { wallpaper ->
                viewModel.onFavoriteClick(wallpaper)
            },
            onPexelLogoClick = { wallpaper ->
                val uri = Uri.parse(wallpaper.url)
                val intent = Intent(Intent.ACTION_VIEW, uri)
                requireActivity().startActivity(intent)
            },
            requireActivity = requireActivity(),
            buttonsVisible = true
        )
    }

    override fun setupViews() {
        setHasOptionsMenu(true)
        binding.apply {
            recyclerView.apply {
                adapter = mAdapter
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
                val divider =
                    DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
                divider.setDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.list_tem_separator
                    )!!
                )
                addItemDecoration(divider)
            }
        }
    }

    override fun setupListeners() {
        binding.toolbarLayout.apply {
            menuButton.setOnClickListener {
                showMenu(menuButton, R.menu.menu_wallpapers)
            }
        }
    }

    override fun setupFlows() {
        binding.apply {
            launchCoroutine {
                viewModel.favorites.collect {
                    val favorites = it ?: return@collect
                    mAdapter?.submitList(favorites)
                    noFavoritesTextview.isVisible = favorites.isEmpty()
                    recyclerView.isVisible = favorites.isNotEmpty()
                }
            }
        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.action_delete_all_favorites -> {
                viewModel.onDeleteAllFavorites()
                true
            }
            else -> false
        }
    }
}