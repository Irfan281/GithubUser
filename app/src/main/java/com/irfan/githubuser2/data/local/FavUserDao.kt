package com.irfan.githubuser2.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavUserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(favUser: FavUser)

    @Query("DELETE FROM favUser WHERE username = :username")
    fun delete(username: String)

    @Query("SELECT * from favUser")
    fun getFavorites(): LiveData<List<FavUser>>

    @Query("SELECT EXISTS(SELECT * FROM favUser WHERE username = :username)")
    fun isFavorite(username: String): Boolean
}