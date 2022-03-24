package com.irfan.githubuser2.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.irfan.githubuser2.data.local.FavUser
import com.irfan.githubuser2.data.local.FavUserRepository
import com.irfan.githubuser2.data.remote.response.ItemsList

class FavoriteViewModel(application: Application) : ViewModel() {
    private val mFavUserRepository: FavUserRepository = FavUserRepository(application)

    fun insert(itemsList: ItemsList) {
        val favoriteUser = FavUser(itemsList.username, itemsList.avatarUrl)
        mFavUserRepository.insert(favoriteUser)
    }

    fun delete(username: String) {
        mFavUserRepository.delete(username)
    }

    fun isFavorite(username: String): Boolean = mFavUserRepository.isFavorite(username)


    fun getFavorites(): LiveData<List<FavUser>> = mFavUserRepository.getFavorites()
}