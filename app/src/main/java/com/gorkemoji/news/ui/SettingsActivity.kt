package com.gorkemoji.news.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.gorkemoji.news.R
import com.gorkemoji.news.databinding.ActivitySettingsBinding
import com.gorkemoji.news.viewmodel.SettingsViewModel

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var viewModel: SettingsViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[SettingsViewModel::class.java]

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

        val apiKey = viewModel.loadApiKey()

        if (apiKey.isNullOrEmpty()) binding.key.text = resources.getText(R.string.empty)
        else binding.key.text = apiKey

        binding.key.text = apiKey

        binding.saveBtn.setOnClickListener {
            if (!binding.keyHint.text.isNullOrBlank()) {
                viewModel.saveApiKey(binding.keyHint.text.toString())
                binding.key.text = binding.keyHint.text
            }
        }
    }
}