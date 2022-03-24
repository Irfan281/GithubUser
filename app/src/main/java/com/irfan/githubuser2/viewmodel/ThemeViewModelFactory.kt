package com.irfan.githubuser2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.irfan.githubuser2.datastore.ThemePreferences

class ThemeViewModelFactory(private val preferences: ThemePreferences) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ThemeViewModel::class.java)) {
            return ThemeViewModel(preferences) as T
        }
        throw IllegalArgumentException("Error ViewModel not found")
    }
}