package com.irfan.githubuser2.response

import com.google.gson.annotations.SerializedName

data class GithubUserResponse(
    @field:SerializedName("avatar_url")
    val avatarUrl: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("login")
    val username: String,

    @field:SerializedName("company")
    val company: String,

    @field:SerializedName("location")
    val location: String,

    @field:SerializedName("followers")
    val followers: Int,

    @field:SerializedName("following")
    val following: Int,

    @field:SerializedName("public_repos")
    val publicRepos: Int,

    @field:SerializedName("following_url")
    val followingUrl: String,

    @field:SerializedName("followers_url")
    val followersUrl: String,

    @field:SerializedName("url")
    val url: String,
)

data class ItemsList(
    @field:SerializedName("login")
    val username: String,

    @field:SerializedName("avatar_url")
    val avatarUrl: String,
)

data class Search(
    @field:SerializedName("total_count")
    val totalCount: Int,

    @field:SerializedName("items")
    val items: List<ItemsList>
)
