package com.irfan.githubuser2.view

import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.irfan.githubuser2.R
import com.irfan.githubuser2.adapter.SectionsPagerAdapter
import com.irfan.githubuser2.databinding.ActivityUserDetailBinding
import com.irfan.githubuser2.response.GithubUserResponse
import com.irfan.githubuser2.viewmodel.UserViewModel
import com.irfan.githubuser2.viewmodel.UserViewModelFactory

class UserDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserDetailBinding
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Detail User"

        val data = intent.getStringExtra(USERNAME)

        val viewModelFactory = UserViewModelFactory(data)
        userViewModel = ViewModelProvider(this, viewModelFactory).get(UserViewModel::class.java)

        startViewModel()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
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
