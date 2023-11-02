package com.dicoding.mystorysubmission.data.api

import com.dicoding.mystorysubmission.data.response.ResponseAddStory
import com.dicoding.mystorysubmission.data.response.ResponseDetail
import com.dicoding.mystorysubmission.data.response.ResponseLogin
import com.dicoding.mystorysubmission.data.response.ResponseRegister
import com.dicoding.mystorysubmission.data.response.ResponseStories
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    suspend fun postRegister(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): ResponseRegister

    @FormUrlEncoded
    @POST("login")
    suspend fun postLogin(
        @Field("email") email: String,
        @Field("password") password: String,
    ): ResponseLogin

    @GET("stories/{id}")
    suspend fun getDetails(@Path("id") id: String): ResponseDetail

    @Multipart
    @POST("stories")
    suspend fun postStory(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") lat: Double? = null,
        @Part("lon") lon: Double? = null
    ): ResponseAddStory

    @GET("stories")
    suspend fun getAllStories(
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null,
        @Query("location") location: Int? = null
    ): ResponseStories

}