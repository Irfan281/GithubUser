package com.irfan.githubuser2.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.irfan.githubuser2.adapter.ListUserAdapter
import com.irfan.githubuser2.data.local.FavoriteUser
import com.irfan.githubuser2.data.remote.response.ItemsList
import com.irfan.githubuser2.databinding.ActivityFavoriteUserBinding
import com.irfan.githubuser2.viewmodel.FavoriteViewModel
import com.irfan.githubuser2.viewmodel.FavoriteViewModelFactory
import okhttp3.internal.toImmutableList

@Suppress("UNCHECKED_CAST")
class FavoriteUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteUserBinding
    private lateinit var favoriteViewModel: FavoriteViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Favorite Users"

        favoriteViewModel = callViewModel(this@FavoriteUserActivity)

        favoriteViewModel.getFavorites().observe(this
        ) { favorite ->
            if (favorite != null) {
                val adapter = ListUserAdapter(convert(favorite))

                binding.apply {
                    rvFavorite.layoutManager = LinearLayoutManager(this@FavoriteUserActivity)
                    rvFavorite.setHasFixedSize(true)
                    rvFavorite.adapter = adapter
                }
            }
            if (favorite.isEmpty()) {
                binding.tvFavoriteStatus.visibility = View.VISIBLE
            }

        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun convert(fav: List<FavoriteUser>): List<ItemsList> {
        val users= ArrayList<ItemsList>()

        for (i in fav){
            val move = ItemsList(
                i.username,
                i.avatar
            )
            users.add(move)
        }
        return users
    }

    private fun callViewModel(activity: AppCompatActivity): FavoriteViewModel {
        val factory = FavoriteViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(FavoriteViewModel::class.java)
    }
}