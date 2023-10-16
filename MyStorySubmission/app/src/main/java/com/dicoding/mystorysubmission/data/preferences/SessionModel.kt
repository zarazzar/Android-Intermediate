package com.dicoding.mystorysubmission.data.preferences

data class SessionModel(
    val name: String,
    val token: String,
    val isLogin: Boolean = false
)
