package com.dicoding.mystorysubmission.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dicoding.mystorysubmission.data.StoryRepository
import com.dicoding.mystorysubmission.data.preferences.SessionModel

class SplashViewModel(private val session: StoryRepository): ViewModel() {
    fun getSession(): LiveData<SessionModel> {
        return session.getSession().asLiveData()
    }
}