package com.dicoding.mystorysubmission.ui.maps

import androidx.lifecycle.ViewModel
import com.dicoding.mystorysubmission.data.StoryRepository

class MapsViewModel(private val repo: StoryRepository): ViewModel(){
    fun getAllLocation() = repo.getAllStoriesWithLocation()
}