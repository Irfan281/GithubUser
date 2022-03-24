package com.irfan.githubuser2.datastore


import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ThemePreferences private constructor(private val dataStore: DataStore<Preferences>) {
    private val THEME = booleanPreferencesKey("theme")

    fun getThemeSetting(): Flow<Boolean> {
        return dataStore.data.map { preference ->
            preference[THEME] ?: false
        }
    }

    suspend fun saveTheme(isDarkThemeActive: Boolean) {
        dataStore.edit { preference ->
            preference[THEME] = isDarkThemeActive
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ThemePreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>): ThemePreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = ThemePreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}