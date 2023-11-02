package com.dicoding.mystorysubmission.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.mystorysubmission.R
import com.dicoding.mystorysubmission.databinding.ActivityMainBinding
import com.dicoding.mystorysubmission.ui.landingScreen.LandingActivity
import com.dicoding.mystorysubmission.ui.maps.MapsActivity
import com.dicoding.mystorysubmission.ui.story.PostStoryActivity
import com.dicoding.mystorysubmission.utlis.ViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val mainViewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this, true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setAdapter()
        setBtnAction()
        showAllStories()
        setTitle()
        menuBar()
    }

    private fun setTitle() {
        mainViewModel.getCurrentSession().observe(this) { result ->
            binding.MTAppBar.title = "${getString(R.string.welcome)} ${result.name}!"
        }
    }

    private fun showAllStories() {
        val adapter = StoriesAdapter()
        binding.rvStory.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )
        mainViewModel.stories.observe(this) {
            adapter.submitData(lifecycle, it)
        }
    }

    private fun setAdapter() {
        binding.rvStory.layoutManager = LinearLayoutManager(this)
    }

    private fun setBtnAction() {
        binding.fabPostStory.setOnClickListener {
            val intentToUpload = Intent(this@MainActivity, PostStoryActivity::class.java)
            startActivity(intentToUpload)
        }
    }

    private fun menuBar() {
        binding.MTAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.language -> {
                    val intentToSettings = Intent(Settings.ACTION_LOCALE_SETTINGS)
                    startActivity(intentToSettings)
                    true
                }

                R.id.logout -> {
                    mainViewModel.logout()
                    val intent = Intent(this@MainActivity, LandingActivity::class.java)
                    startActivity(intent)
                    finish()
                    true
                }

                R.id.maps -> {
                    val intentToMaps = Intent(this@MainActivity, MapsActivity::class.java)
                    startActivity(intentToMaps)
                    true
                }

                else -> false
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

}