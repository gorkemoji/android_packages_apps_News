package com.gorkemoji.news.ui

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.gorkemoji.news.R
import com.gorkemoji.news.databinding.ActivityNewsBinding
import com.gorkemoji.news.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavigationView.selectedItemId = R.id.settings

        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    startActivity(Intent(applicationContext, NewsActivity::class.java))
                    true
                }
                R.id.favorites -> {
                    startActivity(Intent(applicationContext, FavoriteActivity::class.java))
                    true
                }
                else -> false
            }
        }

        val apiKey = loadMode("api_key", "keys")

        if (apiKey.isNullOrEmpty())
            binding.key.text = resources.getText(R.string.empty)
        else
            binding.key.text = apiKey

        binding.key.text = apiKey

        binding.saveBtn.setOnClickListener {
            if (!binding.keyHint.text.isNullOrBlank()) {
                saveMode("api_key", binding.keyHint.text.toString(), "keys")
                binding.key.text = binding.keyHint.text
            }
        }

    }

    private fun loadMode(type: String, file: String): String? {
        val pref : SharedPreferences = applicationContext.getSharedPreferences(file, Context.MODE_PRIVATE)

        return pref.getString(type, "")
    }

    private fun saveMode(type: String, data: String, file: String) {
        val pref : SharedPreferences = applicationContext.getSharedPreferences(file, Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = pref.edit()

        editor.putString(type, data)
        editor.apply()
    }
}