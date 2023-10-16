package com.dicoding.mystorysubmission.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.mystorysubmission.R
import com.dicoding.mystorysubmission.data.Result
import com.dicoding.mystorysubmission.databinding.ActivityMainBinding
import com.dicoding.mystorysubmission.ui.landingScreen.LandingActivity
import com.dicoding.mystorysubmission.ui.story.PostStoryActivity
import com.dicoding.mystorysubmission.utlis.ViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val  mainViewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this,true)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setAdapter()
        setBtnAction()
        showAllStories()
        setTitle()
    }

    private fun setTitle() {
        mainViewModel.getCurrentSession().observe(this) { result ->
            binding.MTAppBar.title = "${getString(R.string.welcome)} ${result.name}!"
        }
    }

    private fun showAllStories() {
       mainViewModel.getAllStories().observe(this) {result ->
           if (result != null) {
               when (result) {
                   is Result.Success -> {
                       showLoading(false)
                       val adapter = StoriesAdapter(result.data.listStory)
                       binding.rvStory.adapter = adapter

                   }
                   is Result.Error -> {
                       showLoading(false)
                       mainViewModel.getCurrentSession().observe(this) { session ->
                           Toast.makeText(this@MainActivity, session.token, Toast.LENGTH_SHORT).show()

                       }
                       Toast.makeText(this@MainActivity, result.error, Toast.LENGTH_SHORT).show()
                   }
                   is Result.Loading -> {
                       showLoading(true)
                   }
               }
           }

       }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.apply {
            if (isLoading) {
                rvStory.visibility = View.INVISIBLE
                pbMain.visibility = View.VISIBLE
            } else {
                rvStory.visibility = View.VISIBLE
                pbMain.visibility = View.GONE
            }
        }
    }

    private fun setAdapter() {
        binding.rvStory.layoutManager = LinearLayoutManager(this)
    }

    private fun setBtnAction() {
        binding.fabPostStory.setOnClickListener {
            val intentToUpload = Intent(this@MainActivity,PostStoryActivity::class.java)
            startActivity(intentToUpload)
        }
    }

    fun changeLanguage(item: MenuItem) {
        val intentToSettings = Intent(Settings.ACTION_LOCALE_SETTINGS)
        startActivity(intentToSettings)
    }

    fun logout(item: MenuItem) {
        mainViewModel.logout()
        val intent = Intent(this@MainActivity, LandingActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onBackPressed(){
        super.onBackPressed()
        finishAffinity()
    }


}