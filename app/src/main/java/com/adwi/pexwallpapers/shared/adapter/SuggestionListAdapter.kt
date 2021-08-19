package com.adwi.pexwallpapers.shared.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.adwi.pexwallpapers.data.local.entity.Suggestion
import com.adwi.pexwallpapers.databinding.LayoutSuggestionItemBinding

class SuggestionListAdapter(
    private val onItemClick: (Suggestion) -> Unit,
    private val onSuggestionDeleteClick: (Suggestion) -> Unit
) : ListAdapter<Suggestion, SuggestionListAdapter.SuggestionViewHolder>(SuggestionsComparator()) {

    private lateinit var binding: LayoutSuggestionItemBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuggestionViewHolder {
        binding =
            LayoutSuggestionItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SuggestionViewHolder(
            binding,
            onItemClick = { position ->
                val suggestion = getItem(position)
                if (suggestion != null) {
                    onItemClick(suggestion)
                }
            },
            onSuggestionDeleteClick = { position ->
                val suggestion = getItem(position)
                if (suggestion != null) {
                    onSuggestionDeleteClick(suggestion)
                }
            })
    }

    override fun onBindViewHolder(holder: SuggestionViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }

    inner class SuggestionViewHolder(
        private val binding: LayoutSuggestionItemBinding,
        private val onItemClick: (Int) -> Unit,
        private val onSuggestionDeleteClick: (Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(suggestion: Suggestion) {
            binding.suggestion = suggestion
            binding.executePendingBindings()
        }

        init {
            binding.apply {
                suggestionTitle.setOnClickListener {
                    val position = bindingAdapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        onItemClick(position)
                    }
                }
                suggestionDeleteButton.setOnClickListener {
                    val position = bindingAdapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        onSuggestionDeleteClick(position)
                    }
                }
            }
        }
    }
}

class SuggestionsComparator : DiffUtil.ItemCallback<Suggestion>() {
    override fun areItemsTheSame(oldItem: Suggestion, newItem: Suggestion) =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: Suggestion, newItem: Suggestion) =
        oldItem == newItem
}
