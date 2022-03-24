package com.irfan.githubuser2.view

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.irfan.githubuser2.R
import com.irfan.githubuser2.adapter.SectionsPagerAdapter
import com.irfan.githubuser2.data.remote.response.GithubUserResponse
import com.irfan.githubuser2.data.remote.response.ItemsList
import com.irfan.githubuser2.databinding.ActivityUserDetailBinding
import com.irfan.githubuser2.datastore.ThemePreferences
import com.irfan.githubuser2.viewmodel.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class UserDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserDetailBinding
    private lateinit var userViewModel: UserViewModel
    private lateinit var favoriteViewModel: FavoriteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setTheme()
        setTabLayout()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Detail User"

        val data = intent.getParcelableExtra<ItemsList>(USERNAME)

        val userViewModelFactory = UserViewModelFactory(data?.username)
        userViewModel = ViewModelProvider(this, userViewModelFactory)[UserViewModel::class.java]
        startViewModel()

        favoriteViewModel = callViewModel(this@UserDetailActivity)
        setFavorite(data)
    }

    private fun setFavorite(data: ItemsList?) {
        CoroutineScope(Dispatchers.IO).launch {
            if (data != null) {
                if (favoriteViewModel.isFavorite(data.username))
                    binding.fbFavorite.setImageDrawable(
                        ContextCompat.getDrawable(
                            applicationContext,
                            R.drawable.ic_favorite
                        )
                    )
                else
                    binding.fbFavorite.setImageDrawable(
                        ContextCompat.getDrawable(
                            applicationContext,
                            R.drawable.ic_favorite_blank
                        )
                    )
            }
        }

        binding.fbFavorite.setOnClickListener {
            if (data != null) {
                if (favoriteViewModel.isFavorite(data.username)) {
                    binding.fbFavorite.setImageDrawable(
                        ContextCompat.getDrawable(
                            applicationContext,
                            R.drawable.ic_favorite_blank
                        )
                    )
                    favoriteViewModel.delete(data.username)
                    Toast.makeText(
                        this,
                        "${data.username} deleted from favorite",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    binding.fbFavorite.setImageDrawable(
                        ContextCompat.getDrawable(
                            applicationContext,
                            R.drawable.ic_favorite
                        )
                    )
                    favoriteViewModel.insert(data)
                    Toast.makeText(this, "${data.username} added to favorite", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
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

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun setTabLayout() {
        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }

    private fun callViewModel(activity: AppCompatActivity): FavoriteViewModel {
        val factory = FavoriteViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[FavoriteViewModel::class.java]
    }

    private fun showLoading(isLoading: Boolean) {
        binding.apply {
            progress.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    private fun startViewModel() {
        userViewModel.user.observe(this) { user ->
            setUser(user)
        }

        userViewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }

        userViewModel.responseStatus.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setUser(user: GithubUserResponse) {
        Glide.with(this).load(user.avatarUrl).into(binding.imgDetailAvatar)
        binding.apply {
            tvDetailName.text = user.name
            tvDetailUsername.text = user.username
            if (user.company != null) {
                tvDetailCompany.text = user.company
                tvDetailCompany.visibility = View.VISIBLE
            }

            if (user.location != null) {
                tvDetailLocation.text = user.location
                tvDetailLocation.visibility = View.VISIBLE
            }
            tvDetailFollower.text = user.followers.toString()
            tvDetailFollowing.text = user.following.toString()
            tvDetailRepository.text = user.publicRepos.toString()
        }
    }

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.text_follower,
            R.string.text_following
        )

        const val USERNAME = "username"
    }
}
