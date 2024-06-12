package com.dicoding.kulitku.view

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferences private constructor(private val dataStore: DataStore<Preferences>) {

    private val NAME_KEY = stringPreferencesKey("name")
    private val TOKEN_KEY = stringPreferencesKey("token")
    private val LOGIN_KEY = booleanPreferencesKey("login_session")

    fun getLoginSession(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[LOGIN_KEY] ?: false
        }
    }

    suspend fun saveLoginSession(loginSession: Boolean) {
        dataStore.edit { preferences ->
            preferences[LOGIN_KEY] = loginSession
        }
    }

    fun getToken(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[TOKEN_KEY] ?: ""
        }
    }

    suspend fun saveToken(token: String) {
        dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
        }
    }

    fun getName(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[NAME_KEY] ?: ""
        }
    }

    suspend fun saveName(name: String) {
        dataStore.edit { preferences ->
            preferences[NAME_KEY] = name
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences.remove(LOGIN_KEY)
            preferences.remove(NAME_KEY)
            preferences.remove(TOKEN_KEY)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>): UserPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}