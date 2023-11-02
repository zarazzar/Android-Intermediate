package com.dicoding.mystorysubmission.utlis

import android.content.Context
import com.dicoding.mystorysubmission.data.StoryRepository
import com.dicoding.mystorysubmission.data.api.ApiConfig
import com.dicoding.mystorysubmission.data.preferences.SessionPreferences
import com.dicoding.mystorysubmission.data.preferences.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {

    fun provideRepository(context: Context): StoryRepository? {
        val preferences = SessionPreferences.getInstance(context.dataStore)
        val userToken = runBlocking {
            preferences.getSession().first().token
        }
        val apiService = ApiConfig.getApiService(userToken)
        return StoryRepository.getInstance(preferences,apiService,true)
    }
}