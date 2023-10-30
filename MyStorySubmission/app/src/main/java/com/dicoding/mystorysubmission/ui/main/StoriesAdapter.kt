package com.dicoding.mystorysubmission.ui.main

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.mystorysubmission.data.response.ListStoryItem
import com.dicoding.mystorysubmission.databinding.ItemStoriesBinding
import com.dicoding.mystorysubmission.ui.detail.DetailActivity

class StoriesAdapter :
    PagingDataAdapter<ListStoryItem, StoriesAdapter.ListViewHolder>(DIFF_CALLBACK) {
    class ListViewHolder(private val binding: ItemStoriesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(listStories: ListStoryItem) {
            Glide.with(binding.root.context)
                .load(listStories.photoUrl)
                .into(binding.ivStory)
            binding.tvUsername.text = listStories.name

            itemView.setOnClickListener {
                val id = listStories.id
                val intentToDetail = Intent(binding.root.context, DetailActivity::class.java)
                intentToDetail.putExtra(DetailActivity.EXTRA_ID, id)
                binding.root.context.startActivity(intentToDetail)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemStoriesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, pos: Int) {
        val story = getItem(pos)
        if (story != null) {
            holder.bind(story)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ListStoryItem,
                newItem: ListStoryItem
            ): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

}