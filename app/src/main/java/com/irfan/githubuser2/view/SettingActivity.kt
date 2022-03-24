package com.irfan.githubuser2.view

import android.content.Context
import android.os.Bundle
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.irfan.githubuser2.databinding.ActivitySettingBinding
import com.irfan.githubuser2.datastore.ThemePreferences
import com.irfan.githubuser2.viewmodel.ThemeViewModel
import com.irfan.githubuser2.viewmodel.ThemeViewModelFactory

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Setting"

        val preference = ThemePreferences.getInstance(dataStore)
        val themeViewModel =
            ViewModelProvider(this, ThemeViewModelFactory(preference))[ThemeViewModel::class.java]

        themeViewModel.getThemeSetting().observe(
            this
        ) { isDarkThemeActive: Boolean ->
            if (isDarkThemeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding.switchMode.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding.switchMode.isChecked = false
            }
        }

        binding.switchMode.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            themeViewModel.saveTheme(isChecked)
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}