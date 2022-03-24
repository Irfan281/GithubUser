package com.irfan.githubuser2.view

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.irfan.githubuser2.R
import com.irfan.githubuser2.adapter.ListUserAdapter
import com.irfan.githubuser2.databinding.ActivityMainBinding
import com.irfan.githubuser2.data.remote.response.ItemsList
import com.irfan.githubuser2.viewmodel.SearchViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val searchViewModel by viewModels<SearchViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayShowHomeEnabled(true)

        startViewModel()

        binding.button.setOnClickListener {
            searchViewModel.searchUser(SearchViewModel.DEFAULT_USERNAME)
            binding.tvNoSearch.visibility = View.GONE
        }

        search()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.actionbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_favorite -> {
                val i = Intent(this, FavoriteUserActivity::class.java)
                startActivity(i)
                true
            }
            R.id.menu_setting -> {
                val i = Intent(this, SettingActivity::class.java)
                startActivity(i)
                true
            }
            else -> true
        }
    }

    private fun search() {
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = binding.searchUser

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.query_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                searchViewModel.searchUser(query)
                searchView.clearFocus()
                binding.tvNoSearch.visibility = View.GONE
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
    }

    private fun startViewModel() {
        searchViewModel.listUser.observe(this) { listUser ->
            val layoutManager = LinearLayoutManager(this)
            binding.rvUser.layoutManager = layoutManager
            setUserData(listUser)
        }

        searchViewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    private fun setUserData(listUsers: List<ItemsList>) {
        val adapter = ListUserAdapter(listUsers)
        if (adapter.itemCount == 0) {
            binding.tvNoSearch.visibility = View.VISIBLE
        }
        binding.rvUser.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        binding.apply {
            progress.visibility = if (isLoading) View.VISIBLE else View.GONE
            rvUser.visibility = if (!isLoading) View.VISIBLE else View.GONE
        }
    }
}