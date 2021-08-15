package com.adwi.pexwallpapers.shared.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.adwi.pexwallpapers.databinding.ChipItemBinding

class ChipListAdapter(
    private val onItemClick: (String) -> Unit
) : ListAdapter<String, ChipListAdapter.ChipViewHolder>(SuggestionsComparator()) {

    private lateinit var binding: ChipItemBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChipViewHolder {
        binding =
            ChipItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChipViewHolder(
            binding,
            onItemClick = { position ->
                val string = getItem(position)
                if (string != null) {
                    onItemClick(string)
                }
            })
    }

    override fun onBindViewHolder(holder: ChipViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }

    inner class ChipViewHolder(
        private val binding: ChipItemBinding,
        private val onItemClick: (Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: String) {
            binding.item = item
            binding.executePendingBindings()
        }

        init {
            binding.apply {
                root.setOnClickListener {
                    val position = bindingAdapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        onItemClick(position)
                    }
                }
            }
        }
    }

    class SuggestionsComparator : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: String, newItem: String) =
            oldItem == newItem
    }
}
