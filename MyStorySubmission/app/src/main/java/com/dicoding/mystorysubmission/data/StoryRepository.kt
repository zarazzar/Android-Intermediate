package com.dicoding.mystorysubmission.data

import androidx.lifecycle.liveData
import com.dicoding.mystorysubmission.data.api.ApiService
import com.dicoding.mystorysubmission.data.preferences.SessionModel
import com.dicoding.mystorysubmission.data.preferences.SessionPreferences
import com.dicoding.mystorysubmission.data.response.ResponseAddStory
import com.dicoding.mystorysubmission.data.response.ResponseDetail
import com.dicoding.mystorysubmission.data.response.ResponseLogin
import com.dicoding.mystorysubmission.data.response.ResponseRegister
import com.dicoding.mystorysubmission.data.response.ResponseStories
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
    private val apiService: ApiService
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

    fun register(name: String, email: String, pass: String) = liveData {
        emit(Result.Loading)
        try {
            val responseSuccess = apiService.postRegister(name, email, pass)
            emit(Result.Success(responseSuccess))
        } catch (exc: HttpException) {
            val error = exc.response()?.errorBody()?.string()
            val response = Gson().fromJson(error, ResponseRegister::class.java)
            emit(Result.Error(response.message))
        }
    }

    fun login(email: String, pass: String) = liveData {
        emit(Result.Loading)
        try {
            val responseSuccess = apiService.postLogin(email, pass)
            val userLogin = SessionModel(
                responseSuccess.loginResult.name,
                responseSuccess.loginResult.token,
                true
            )
            saveSession(userLogin)
            emit(Result.Success(responseSuccess))
        } catch (exc: HttpException) {
            val error = exc.response()?.errorBody()?.string()
            val response = Gson().fromJson(error, ResponseLogin::class.java)
            emit(Result.Error(response.message))
        }
    }

    fun getAllStories() = liveData {
        emit(Result.Loading)
        try {
            val responseSuccess = apiService.getAllStories()
            emit(Result.Success(responseSuccess))
        } catch (exc: HttpException) {
            val error = exc.response()?.errorBody()?.string()
            val response = Gson().fromJson(error, ResponseStories::class.java)
            emit(Result.Error(response.message))
        }
    }

    fun postStory(img: File, desc: String) = liveData {
        emit(Result.Loading)
        val requestImageFile = img.asRequestBody("image/jpeg".toMediaType())
        val requestBody = desc.toRequestBody("text/plain".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "photo",
            img.name,
            requestImageFile
        )
        try {
            val responseSuccess = apiService.postStory(multipartBody, requestBody)
            emit(Result.Success(responseSuccess))
        } catch (exc: HttpException) {
            val error = exc.response()?.errorBody()?.string()
            val response = Gson().fromJson(error, ResponseAddStory::class.java)
            emit(Result.Error(response.message))
        }
    }

    fun getDetail(id: String) = liveData {
        emit(Result.Loading)
        try {
            val responseSuccess = apiService.getDetails(id)
            emit(Result.Success(responseSuccess))
        } catch (exc: HttpException) {
            val error = exc.response()?.errorBody()?.string()
            val response = Gson().fromJson(error, ResponseDetail::class.java)
            emit(Result.Error(response.message))
        }

    }

    companion object {
        @Volatile
        private var instance: StoryRepository? = null
        fun getInstance(
            preferences: SessionPreferences,
            apiService: ApiService,
            tokenState: Boolean
        ): StoryRepository? {
            if (tokenState) {
                synchronized(this) {
                    instance = StoryRepository(preferences, apiService)
                }
            }
            return instance
        }

    }
}
