package com.dicoding.mystorysubmission.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.mystorysubmission.data.StoryRepository
import com.dicoding.mystorysubmission.data.preferences.SessionModel
import kotlinx.coroutines.launch

class MainViewModel(private val repo: StoryRepository) : ViewModel() {

    fun getCurrentSession(): LiveData<SessionModel> {
        return repo.getSession().asLiveData()
    }

    fun logout(){
        viewModelScope.launch { repo.logout() }
    }

    fun getAllStories() = repo.getAllStories()
}