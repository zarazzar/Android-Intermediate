package com.dicoding.mystorysubmission.ui.main

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.mystorysubmission.data.response.ListStoryItem
import com.dicoding.mystorysubmission.databinding.ItemStoriesBinding
import com.dicoding.mystorysubmission.ui.detail.DetailActivity

class StoriesAdapter(private val storiesLists: List<ListStoryItem>) :
    RecyclerView.Adapter<StoriesAdapter.ListViewHolder>() {
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

    override fun getItemCount(): Int = storiesLists.size

    override fun onBindViewHolder(holder: ListViewHolder, pos: Int) {
        val story = storiesLists[pos]
        holder.bind(story)
    }
}