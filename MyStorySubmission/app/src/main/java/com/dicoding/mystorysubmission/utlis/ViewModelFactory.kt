package com.dicoding.mystorysubmission.utlis

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.mystorysubmission.data.StoryRepository
import com.dicoding.mystorysubmission.ui.detail.DetailViewModel
import com.dicoding.mystorysubmission.ui.login.LoginViewModel
import com.dicoding.mystorysubmission.ui.main.MainViewModel
import com.dicoding.mystorysubmission.ui.maps.MapsViewModel
import com.dicoding.mystorysubmission.ui.register.RegistryViewModel
import com.dicoding.mystorysubmission.ui.splash.SplashViewModel
import com.dicoding.mystorysubmission.ui.story.PostViewModel
import java.lang.IllegalArgumentException

class ViewModelFactory(private val storyRepository: StoryRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(SplashViewModel::class.java) -> {
                SplashViewModel(storyRepository) as T
            }

            modelClass.isAssignableFrom(RegistryViewModel::class.java) -> {
                RegistryViewModel(storyRepository) as T
            }

            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(storyRepository) as T
            }

            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(storyRepository) as T
            }

            modelClass.isAssignableFrom(PostViewModel::class.java) -> {
                PostViewModel(storyRepository) as T
            }

            modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
                DetailViewModel(storyRepository) as T
            }

            modelClass.isAssignableFrom(MapsViewModel::class.java) -> {
                MapsViewModel(storyRepository) as T
            }

            else -> throw IllegalArgumentException("ViewModel class unavailable: ${modelClass.name}")
        }
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context, tokenState: Boolean): ViewModelFactory {
            synchronized(this) {
                if (instance == null || tokenState) {
                    instance = Injection.provideRepository(context)?.let { ViewModelFactory(it) }
                }
            }
            return instance as ViewModelFactory
        }
    }

}