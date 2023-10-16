package com.dicoding.mystorysubmission.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.mystorysubmission.data.StoryRepository
import com.dicoding.mystorysubmission.data.preferences.SessionModel
import kotlinx.coroutines.launch

class LoginViewModel (private val repo: StoryRepository) : ViewModel() {

    fun loginUser(email: String, pass: String) = repo.login(email, pass)

    fun saveSessionUser(login: SessionModel) {
        viewModelScope.launch { repo.saveSession(login) }
    }
}