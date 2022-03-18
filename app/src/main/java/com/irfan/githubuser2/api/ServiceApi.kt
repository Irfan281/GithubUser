package com.irfan.githubuser2.api

import com.irfan.githubuser2.response.GithubUserResponse
import com.irfan.githubuser2.response.ItemsList
import com.irfan.githubuser2.response.Search
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ServiceApi {
    @GET("users/{username}")
    fun getDetailUser(
        @Path("username") username: String?
    ): Call<GithubUserResponse>

    @GET("users/{username}/followers")
    fun getFollower(
        @Path("username") username: String?
    ): Call<List<ItemsList>>

    @GET("users/{username}/following")
    fun getFollowing(
        @Path("username") username: String?
    ): Call<List<ItemsList>>


    @GET("search/users")
    fun getSearch(
        @Query("q") username: String
    ): Call<Search>
}