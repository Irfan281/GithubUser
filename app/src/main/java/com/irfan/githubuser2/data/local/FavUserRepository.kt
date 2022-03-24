package com.irfan.githubuser2.data.local

import android.app.Application
import androidx.lifecycle.LiveData
import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavUserRepository(application: Application) {
    private val mFavUser: FavUserDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = FavUserRoomDatabase.getDatabase(application)
        mFavUser = db.favoriteUserDao()
    }

    fun getFavorites(): LiveData<List<FavUser>> = mFavUser.getFavorites()

    fun insert(favUser: FavUser) {
        executorService.execute { mFavUser.insert(favUser) }
    }

    fun delete(username: String) {
        executorService.execute { mFavUser.delete(username) }
    }

    fun isFavorite(username: String): Boolean {
        val future = executorService.submit(Callable { mFavUser.isFavorite(username) })

        return future.get()
    }
}