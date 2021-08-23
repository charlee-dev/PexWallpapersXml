package com.adwi.pexwallpapers.ui.preview.bottomsheet

import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.adwi.pexwallpapers.data.local.entity.Wallpaper
import com.adwi.pexwallpapers.databinding.FragmentBottomSheetBinding
import com.adwi.pexwallpapers.shared.adapter.WallpaperListAdapter
import com.adwi.pexwallpapers.shared.base.BaseBottomSheet
import com.adwi.pexwallpapers.shared.tools.SharingTools
import com.adwi.pexwallpapers.shared.tools.UrlTools
import com.adwi.pexwallpapers.util.launchCoroutine
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class BottomSheetFragment : BaseBottomSheet<FragmentBottomSheetBinding, WallpaperListAdapter>(
    inflate = FragmentBottomSheetBinding::inflate
) {
    override val viewModel: BottomSheetViewModel by viewModels()
    override val args: BottomSheetFragmentArgs by navArgs()

    private lateinit var wallpaperArgs: Wallpaper

    override fun setupAdapters() {
        _mAdapter = WallpaperListAdapter(
            requireActivity = requireActivity(),
            onItemClick = { wallpaper ->
                findNavController().navigate(
                    BottomSheetFragmentDirections.actionBottomSheetFragmentToPreviewFragment(
                        wallpaper
                    )
                )
            },
            itemRandomHeight = false
        )
    }

    override fun setupViews() {
        binding.apply {
            wallpaperArgs = args.wallpaper
            wallpaper = wallpaperArgs
            executePendingBindings()
            invalidateAll()

            recyclerView.apply {
                adapter = _mAdapter
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                setHasFixedSize(true)
            }
        }
    }

    override fun setupListeners() {
        binding.apply {
            pexelsButton.setOnClickListener {
                UrlTools(requireContext()).openUrlInBrowser(wallpaperArgs.url!!)
            }
            shareButton.setOnClickListener {
                SharingTools(requireContext()).share(wallpaperArgs.imageUrl)
            }
            favoritesBookmark.setOnClickListener {
                launchCoroutine {
                    viewModel.onFavoriteClick(wallpaperArgs)
                }
            }
            setWallpaperButton.setOnClickListener {
                findNavController().navigate(
                    BottomSheetFragmentDirections.actionBottomSheetFragmentToSetWallpaperFragment(
                        wallpaperArgs
                    )
                )
            }
        }
    }

    override fun setupFlows() {
        binding.apply {
            launchCoroutine {
                viewModel.getWallpaper(wallpaperArgs.id).collect {
                    val wallpaperFlow = it
                    wallpaper = wallpaperFlow
                }
            }
            launchCoroutine {
                viewModel.onCategoryNameSubmit(wallpaperArgs.categoryName)
                viewModel.wallpaperResults.collect {
                    val wallpapers = it ?: return@collect
                    shimmerHorizontal.apply {
                        if (wallpapers.isNullOrEmpty()) startShimmer() else stopShimmer()
                        isVisible = wallpapers.isNullOrEmpty()
                    }
                    recyclerView.isVisible = wallpapers.isNotEmpty()
                    noResultsTextview.isVisible = wallpapers.isEmpty()
                    _mAdapter?.submitList(wallpapers)
                }
            }
        }
    }
}