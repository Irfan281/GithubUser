package com.irfan.githubuser2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory

class UserViewModelFactory(private var name: String?) : NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            return UserViewModel(name) as T
        }
        throw IllegalArgumentException("error not found")
    }
}