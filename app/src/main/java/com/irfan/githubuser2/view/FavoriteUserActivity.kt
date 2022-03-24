package com.irfan.githubuser2.view

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.irfan.githubuser2.adapter.ListUserAdapter
import com.irfan.githubuser2.data.local.FavUser
import com.irfan.githubuser2.data.remote.response.ItemsList
import com.irfan.githubuser2.databinding.ActivityFavoriteUserBinding
import com.irfan.githubuser2.datastore.ThemePreferences
import com.irfan.githubuser2.viewmodel.FavoriteViewModel
import com.irfan.githubuser2.viewmodel.FavoriteViewModelFactory
import com.irfan.githubuser2.viewmodel.ThemeViewModel
import com.irfan.githubuser2.viewmodel.ThemeViewModelFactory

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@Suppress("UNCHECKED_CAST")
class FavoriteUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteUserBinding
    private lateinit var favoriteViewModel: FavoriteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setTheme()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Favorite User"

        favoriteViewModel = callViewModel(this@FavoriteUserActivity)

        favoriteViewModel.getFavorites().observe(
            this
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

    private fun setTheme() {
        val preference = ThemePreferences.getInstance(dataStore)
        val themeViewModel =
            ViewModelProvider(this, ThemeViewModelFactory(preference))[ThemeViewModel::class.java]

        themeViewModel.getThemeSetting().observe(
            this
        ) { isDarkThemeActive: Boolean ->
            if (isDarkThemeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    private fun convert(fav: List<FavUser>): List<ItemsList> {
        val users = ArrayList<ItemsList>()

        for (i in fav) {
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
        return ViewModelProvider(activity, factory)[FavoriteViewModel::class.java]
    }
}