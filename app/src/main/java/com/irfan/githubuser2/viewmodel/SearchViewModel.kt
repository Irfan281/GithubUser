package com.irfan.githubuser2.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.irfan.githubuser2.api.ConfigApi
import com.irfan.githubuser2.response.ItemsList
import com.irfan.githubuser2.response.Search
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchViewModel : ViewModel() {
    private val _listUser = MutableLiveData<List<ItemsList>>()
    val listUser: LiveData<List<ItemsList>> = _listUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        searchUser(DEFAULT_USERNAME)
    }

    fun searchUser(username: String) {
        _isLoading.value = true

        val client = ConfigApi.getApiService().getSearch(username)
        client.enqueue(object : Callback<Search> {
            override fun onResponse(call: Call<Search>, response: Response<Search>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listUser.value = response.body()?.items

                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<Search>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    companion object {
        const val TAG = "MainViewModel"
        const val DEFAULT_USERNAME = "followers:>3000"
    }
}