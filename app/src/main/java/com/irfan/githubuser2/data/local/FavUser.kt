package com.irfan.githubuser2.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FavUser(
    @PrimaryKey
    @ColumnInfo(name = "username")
    var username: String,

    @ColumnInfo(name = "avatar")
    var avatar: String
)