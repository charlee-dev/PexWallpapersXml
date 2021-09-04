package com.adwi.pexwallpapers.ui.setwallpaper.bottomsheet

import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.adwi.pexwallpapers.data.local.entity.Wallpaper
import com.adwi.pexwallpapers.databinding.FragmentBottomSheetBinding
import com.adwi.pexwallpapers.shared.adapter.WallpaperListAdapter
import com.adwi.pexwallpapers.shared.tools.SharingTools
import com.adwi.pexwallpapers.ui.base.BaseBottomSheet
import com.adwi.pexwallpapers.util.launchCoroutine
import com.adwi.pexwallpapers.util.showSnackbar
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
            onItemClick = { wallpaper ->
                findNavController().navigate(
                    BottomSheetFragmentDirections.actionBottomSheetFragmentToPreviewFragment(
                        wallpaper
                    )
                )
            },
            onItemLongClick = { wallpaper ->
                viewModel.onFavoriteClick(wallpaper)
            },
            itemRandomHeight = false,
        )
    }

    override fun setupViews() {
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
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
            buttonsBottomSheet.apply {
                pexelsButtonBottomSheet.setOnClickListener {
                    SharingTools(requireContext()).openUrlInBrowser(wallpaperArgs.url!!)
                }

                shareButtonBottomSheet.setOnClickListener {
                    launchCoroutine {
                        SharingTools(requireContext()).shareImage(
                            wallpaperArgs.imageUrl,
                            wallpaperArgs.photographer
                        )
                    }
                }
                downloadButtonBottomSheet.setOnClickListener {
                    launchCoroutine {
                        SharingTools(requireContext()).saveImageLocally(
                            wallpaperArgs.imageUrl,
                            wallpaperArgs.photographer
                        )
                        showSnackbar(
                            "Saved to Gallery - Photo by ${wallpaperArgs.photographer}",
                            view = root.rootView
                        )
                    }
                }
                favoritesBookmarkBottomSheet.setOnClickListener {
                    launchCoroutine {
                        viewModel.onFavoriteClick(wallpaperArgs)
                    }
                }
            }
        }
    }

    override fun setupFlows() {
        binding.apply {
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