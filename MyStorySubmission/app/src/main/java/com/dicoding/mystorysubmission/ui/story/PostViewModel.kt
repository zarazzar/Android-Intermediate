package com.dicoding.mystorysubmission.ui.story

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.mystorysubmission.data.StoryRepository
import com.dicoding.mystorysubmission.data.preferences.LocationModel
import kotlinx.coroutines.launch
import java.io.File

class PostViewModel(private val repo: StoryRepository) : ViewModel() {

    fun postStory(img: File, desc: String, lat: Double?, lon: Double?) =
        repo.postStory(img, desc, lat, lon)

    fun getLocationSession(): LiveData<LocationModel> {
        return repo.getSessionLocation().asLiveData()
    }

    fun saveLocationSession(location: LocationModel) {
        viewModelScope.launch { repo.saveSessionLocation(location) }
    }


}