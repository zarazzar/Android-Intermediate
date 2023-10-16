package com.dicoding.mystorysubmission.ui.story

import androidx.lifecycle.ViewModel
import com.dicoding.mystorysubmission.data.StoryRepository
import java.io.File

class PostViewModel(private val repo: StoryRepository) : ViewModel() {

    fun postStory(img: File, desc: String) = repo.postStory(img, desc)

}