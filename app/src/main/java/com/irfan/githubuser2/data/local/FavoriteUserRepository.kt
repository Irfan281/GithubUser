package com.irfan.githubuser2.data.local

import android.app.Application
import androidx.lifecycle.LiveData
import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteUserRepository(application: Application) {
    private val mFavoriteUser: FavoriteUserDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = FavoriteUserRoomDatabase.getDatabase(application)
        mFavoriteUser = db.favoriteUserDao()
    }

    fun getFavorites(): LiveData<List<FavoriteUser>> = mFavoriteUser.getFavorites()

    fun insert(favoriteUser: FavoriteUser) {
        executorService.execute { mFavoriteUser.insert(favoriteUser) }
    }

    fun delete(username: String) {
        executorService.execute { mFavoriteUser.delete(username) }
    }

    fun isFavorite(username: String): Boolean {
        val future = executorService.submit(Callable { mFavoriteUser.isFavorite(username) })

        return future.get()
    }
}