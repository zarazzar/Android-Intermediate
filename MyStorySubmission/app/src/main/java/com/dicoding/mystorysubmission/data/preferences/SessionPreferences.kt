package com.dicoding.mystorysubmission.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.Flow
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "preferences")

class SessionPreferences private constructor(private val dataStore: DataStore<Preferences>) {

    fun getSession(): Flow<SessionModel> {
        return dataStore.data.map { pref ->
            SessionModel(
                pref[NAME_KEY] ?: "",
                pref[TOKEN_KEY] ?: "",
                pref[LOGIN_KEY] ?: false
            )
        }
    }

    suspend fun saveSession(users: SessionModel) {
        dataStore.edit { pref ->
            pref[NAME_KEY] = users.name
            pref[TOKEN_KEY] = users.token
            pref[LOGIN_KEY] = true

        }
    }

    suspend fun logout() {
        dataStore.edit { pref ->
            pref.clear()
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: SessionPreferences? = null
        private val NAME_KEY = stringPreferencesKey("name")
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val LOGIN_KEY = booleanPreferencesKey("isLogin")

        fun getInstance(dataStore: DataStore<Preferences>): SessionPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = SessionPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }


    }
}