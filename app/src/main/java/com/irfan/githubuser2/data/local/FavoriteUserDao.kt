package com.irfan.githubuser2.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.irfan.githubuser2.data.remote.response.ItemsList

@Dao
interface FavoriteUserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(favoriteUser: FavoriteUser)

    @Query("DELETE FROM favoriteuser WHERE username = :username")
    fun delete(username: String)

    @Query("SELECT * from favoriteUser")
    fun getFavorites(): LiveData<List<FavoriteUser>>

    @Query("SELECT EXISTS(SELECT * FROM favoriteUser WHERE username = :username)")
    fun isFavorite(username: String): Boolean
}