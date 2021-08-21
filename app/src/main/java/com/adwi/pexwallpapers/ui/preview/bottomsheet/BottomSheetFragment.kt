package com.adwi.pexwallpapers.ui.preview.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.adwi.pexwallpapers.data.local.entity.Wallpaper
import com.adwi.pexwallpapers.databinding.FragmentBottomSheetBinding
import com.adwi.pexwallpapers.shared.adapter.WallpaperListAdapter
import com.adwi.pexwallpapers.shared.tools.SharingTools
import com.adwi.pexwallpapers.shared.tools.UrlTools
import com.adwi.pexwallpapers.util.launchCoroutine
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class BottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var wallpaperArgs: Wallpaper

    private var _binding: FragmentBottomSheetBinding? = null
    private val binding get() = _binding!!

    private var _mAdapter: WallpaperListAdapter? = null
    private val mAdapter get() = _mAdapter!!

    private val viewModel: BottomSheetViewModel by viewModels()
    private val args: BottomSheetFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapters()
        setupViews()
        setupListeners()
        setupFlows()
    }

    private fun setupAdapters() {
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

    private fun setupViews() {
        binding.apply {
            wallpaperArgs = args.wallpaper
            wallpaper = wallpaperArgs
            executePendingBindings()
            invalidateAll()

            recyclerView.apply {
                adapter = mAdapter
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                setHasFixedSize(true)
            }
        }
    }

    private fun setupListeners() {
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

    private fun setupFlows() {
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
                    mAdapter.submitList(wallpapers)
                    noResultsTextview.isVisible = wallpapers.isEmpty()
                    recyclerView.isVisible = wallpapers.isNotEmpty()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _mAdapter = null
    }
}