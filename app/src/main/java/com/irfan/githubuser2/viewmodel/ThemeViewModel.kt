package com.irfan.githubuser2.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.irfan.githubuser2.datastore.ThemePreferences
import kotlinx.coroutines.launch

class ThemeViewModel(private val preferences: ThemePreferences) : ViewModel() {
    fun getThemeSetting(): LiveData<Boolean> {
        return preferences.getThemeSetting().asLiveData()
    }

    fun saveTheme(isDarkThemeActive: Boolean) {
        viewModelScope.launch {
            preferences.saveTheme(isDarkThemeActive)
        }
    }
}