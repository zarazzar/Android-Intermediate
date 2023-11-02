package com.dicoding.mystorysubmission.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.dicoding.mystorysubmission.R
import com.dicoding.mystorysubmission.data.Result
import com.dicoding.mystorysubmission.databinding.ActivityDetailBinding
import com.dicoding.mystorysubmission.utlis.ViewModelFactory

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    private val detailViewModel by viewModels<DetailViewModel> {
        ViewModelFactory.getInstance(this, true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setData()
    }

    private fun setData() {
        intent.getStringExtra(EXTRA_ID)?.let {
            detailViewModel.getDetails(it).observe(this) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Success -> {
                            showLoading(false)
                            binding.apply {
                                Glide.with(root.context)
                                    .load(result.data.story.photoUrl)
                                    .into(ivGambar)
                                tvUsername.text = result.data.story.name
                                tvDescription.text = result.data.story.description

                                supportActionBar?.apply {
                                    title = "${getString(R.string.detail_bar)} ${result.data.story.name}"
                                    setDisplayHomeAsUpEnabled(true)
                                }
                            }
                        }

                        is Result.Error -> {
                            showLoading(false)
                            Toast.makeText(this@DetailActivity, result.error, Toast.LENGTH_SHORT)
                                .show()
                        }

                        is Result.Loading -> {
                            showLoading(true)
                        }
                    }
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.apply {
            if (isLoading) {
                pbDetail.visibility = View.VISIBLE
                ivGambar.visibility = View.INVISIBLE
                tvDescription.visibility = View.INVISIBLE
                tvUsername.visibility = View.INVISIBLE
                ivIcon.visibility = View.INVISIBLE
                titleFrom.visibility = View.INVISIBLE

            } else {
                pbDetail.visibility = View.GONE
                ivGambar.visibility = View.VISIBLE
                tvDescription.visibility = View.VISIBLE
                tvUsername.visibility = View.VISIBLE
                ivIcon.visibility = View.VISIBLE
                titleFrom.visibility = View.VISIBLE
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        finish()
        return super.onSupportNavigateUp()
    }

    companion object {
        const val EXTRA_ID = "extra_id"
    }
}