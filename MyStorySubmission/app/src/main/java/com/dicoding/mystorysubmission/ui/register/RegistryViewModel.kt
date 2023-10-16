package com.dicoding.mystorysubmission.ui.register

import androidx.lifecycle.ViewModel
import com.dicoding.mystorysubmission.data.StoryRepository

class RegistryViewModel(private val repo: StoryRepository) : ViewModel() {

    fun registerUser(name: String, email: String, pass: String) = repo.register(name, email, pass)

}