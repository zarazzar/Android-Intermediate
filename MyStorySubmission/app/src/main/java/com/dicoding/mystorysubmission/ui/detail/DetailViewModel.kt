package com.dicoding.mystorysubmission.ui.detail

import androidx.lifecycle.ViewModel
import com.dicoding.mystorysubmission.data.StoryRepository

class DetailViewModel(private val repo: StoryRepository): ViewModel() {

    fun getDetails(id: String) = repo.getDetail(id)

}