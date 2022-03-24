package com.irfan.githubuser2.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.irfan.githubuser2.data.local.FavoriteUser
import com.irfan.githubuser2.data.local.FavoriteUserRepository
import com.irfan.githubuser2.data.remote.response.ItemsList

class FavoriteViewModel(application: Application) : ViewModel() {
    private val mFavoriteUserRepository: FavoriteUserRepository = FavoriteUserRepository(application)

    fun insert(itemsList: ItemsList){
        val favoriteUser = FavoriteUser(itemsList.username, itemsList.avatarUrl)
        mFavoriteUserRepository.insert(favoriteUser)
    }

    fun delete(username: String){
        mFavoriteUserRepository.delete(username)
    }

    fun isFavorite(username: String):Boolean = mFavoriteUserRepository.isFavorite(username)


    fun getFavorites(): LiveData<List<FavoriteUser>> = mFavoriteUserRepository.getFavorites()
}