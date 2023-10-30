package com.dicoding.mystorysubmission.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.dicoding.mystorysubmission.data.api.ApiService
import com.dicoding.mystorysubmission.data.database.StoriesDatabase
import com.dicoding.mystorysubmission.data.database.StoryRemoteMediator
import com.dicoding.mystorysubmission.data.preferences.LocationModel
import com.dicoding.mystorysubmission.data.preferences.SessionModel
import com.dicoding.mystorysubmission.data.preferences.SessionPreferences
import com.dicoding.mystorysubmission.data.response.ListStoryItem
import com.dicoding.mystorysubmission.data.response.ResponseAddStory
import com.dicoding.mystorysubmission.data.response.ResponseDetail
import com.dicoding.mystorysubmission.data.response.ResponseLogin
import com.dicoding.mystorysubmission.data.response.ResponseRegister
import com.dicoding.mystorysubmission.data.response.ResponseStories
import com.dicoding.mystorysubmission.utlis.wrapEspressoIdlingResource
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File

class StoryRepository private constructor(
    private val sessionPref: SessionPreferences,
    private val apiService: ApiService,
    private val storiesDatabase: StoriesDatabase
) {
    fun getSession(): Flow<SessionModel> {
        return sessionPref.getSession()
    }

    suspend fun saveSession(login: SessionModel) {
        sessionPref.saveSession(login)
    }

    suspend fun logout() {
        sessionPref.logout()
    }

    fun getSessionLocation(): Flow<LocationModel> {
        return sessionPref.getSessionLocation()
    }

    suspend fun saveSessionLocation(location: LocationModel) {
        sessionPref.saveSessionLocation(location)
    }

    fun register(name: String, email: String, pass: String) = liveData {
        emit(Result.Loading)
        try {
            val responseSuccess = apiService.postRegister(name, email, pass)
            emit(Result.Success(responseSuccess))
        } catch (exc: Exception) {
            if (exc is HttpException) {
                val error = exc.response()?.errorBody()?.string()
                val response = Gson().fromJson(error, ResponseRegister::class.java)
                emit(Result.Error(response.message))
            }
        }
    }

    fun login(email: String, pass: String) = liveData {
        emit(Result.Loading)
        wrapEspressoIdlingResource {
            try {
                val responseSuccess = apiService.postLogin(email, pass)
                val userLogin = SessionModel(
                    responseSuccess.loginResult.name,
                    responseSuccess.loginResult.token,
                    true
                )
                saveSession(userLogin)
                emit(Result.Success(responseSuccess))
            } catch (exc: Exception) {
                if (exc is HttpException) {
                    val error = exc.response()?.errorBody()?.string()
                    val response = Gson().fromJson(error, ResponseLogin::class.java)
                    emit(Result.Error(response.message))
                }
            }
        }
    }

    fun getAllStories(): LiveData<PagingData<ListStoryItem>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(storiesDatabase, apiService),
            pagingSourceFactory = {
                storiesDatabase.storyDao().getAllStories()
            }
        ).liveData
    }

    fun getAllStoriesWithLocation() = liveData {
        emit(Result.Loading)
        try {
            val responseSuccess = apiService.getAllStories(1, 50, 1)
            emit(Result.Success(responseSuccess))
        } catch (exc: Exception) {
            if (exc is HttpException) {
                val error = exc.response()?.errorBody()?.string()
                val response = Gson().fromJson(error, ResponseStories::class.java)
                emit(Result.Error(response.message))
            } else {
                emit(Result.Error("Something Wrong"))
            }
        }
    }

    fun postStory(img: File, desc: String, lat: Double?, lon: Double?) = liveData {
        emit(Result.Loading)
        val requestImageFile = img.asRequestBody("image/jpeg".toMediaType())
        val requestBody = desc.toRequestBody("text/plain".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "photo",
            img.name,
            requestImageFile
        )

        try {
            val responseSuccess = if (lat == null && lon == null) {
                apiService.postStory(multipartBody, requestBody)
            } else {
                apiService.postStory(multipartBody, requestBody, lat, lon)
            }
            emit(Result.Success(responseSuccess))
        } catch (exc: Exception) {
            if (exc is HttpException) {
                val error = exc.response()?.errorBody()?.string()
                val response = Gson().fromJson(error, ResponseAddStory::class.java)
                emit(Result.Error(response.message))
            } else {
                emit(Result.Error("Something Wrong"))
            }
        }
    }

    fun getDetail(id: String) = liveData {
        emit(Result.Loading)
        try {
            val responseSuccess = apiService.getDetails(id)
            emit(Result.Success(responseSuccess))
        } catch (exc: Exception) {
            if (exc is HttpException) {
                val error = exc.response()?.errorBody()?.string()
                val response = Gson().fromJson(error, ResponseDetail::class.java)
                emit(Result.Error(response.message))
            } else {
                emit(Result.Error("Something Wrong"))
            }
        }
    }

    companion object {
        @Volatile
        private var instance: StoryRepository? = null
        fun getInstance(
            preferences: SessionPreferences,
            apiService: ApiService,
            storiesDatabase: StoriesDatabase,
            tokenState: Boolean
        ): StoryRepository? {
            if (instance == null || tokenState) {
                synchronized(this) {
                    instance = StoryRepository(preferences, apiService, storiesDatabase)
                }
            }
            return instance
        }
    }
}
