package com.irfan.githubuser2.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.irfan.githubuser2.data.remote.api.ConfigApi
import com.irfan.githubuser2.data.remote.response.GithubUserResponse
import com.irfan.githubuser2.data.remote.response.ItemsList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserViewModel(name: String?) : ViewModel() {
    private val _user = MutableLiveData<GithubUserResponse>()
    val user: LiveData<GithubUserResponse> = _user

    private val _listFollower = MutableLiveData<List<ItemsList>>()
    val listFollower: LiveData<List<ItemsList>> = _listFollower

    private val _listFollowing = MutableLiveData<List<ItemsList>>()
    val listFollowing: LiveData<List<ItemsList>> = _listFollowing

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val username = name

    init {
        getUser()
        setTabLayout()
    }

    private fun setTabLayout() {
        viewModelScope.launch(Dispatchers.IO) {
            follower()
        }

        viewModelScope.launch(Dispatchers.IO) {
            following()
        }
    }

    private fun getUser() {
        _isLoading.value = true

        val client = ConfigApi.getApiService().getDetailUser(username)
        client.enqueue(object : Callback<GithubUserResponse> {
            override fun onResponse(
                call: Call<GithubUserResponse>,
                response: Response<GithubUserResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _user.value = response.body()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<GithubUserResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun follower() {
        val client = ConfigApi.getApiService().getFollower(username)
        client.enqueue(object : Callback<List<ItemsList>> {
            override fun onResponse(
                call: Call<List<ItemsList>>,
                response: Response<List<ItemsList>>
            ) {
                if (response.isSuccessful) {
                    _listFollower.value = response.body()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<ItemsList>>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun following() {
        val client = ConfigApi.getApiService().getFollowing(username)
        client.enqueue(object : Callback<List<ItemsList>> {
            override fun onResponse(
                call: Call<List<ItemsList>>,
                response: Response<List<ItemsList>>
            ) {
                if (response.isSuccessful) {
                    _listFollowing.value = response.body()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<ItemsList>>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    companion object {
        private const val TAG = "MainViewModel"
    }
}