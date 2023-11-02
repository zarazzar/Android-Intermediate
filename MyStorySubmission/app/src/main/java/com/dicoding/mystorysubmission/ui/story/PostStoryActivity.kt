package com.dicoding.mystorysubmission.ui.story

import android.net.Uri
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.dicoding.mystorysubmission.R
import com.dicoding.mystorysubmission.data.Result
import com.dicoding.mystorysubmission.databinding.ActivityPostStoryBinding
import com.dicoding.mystorysubmission.ui.main.MainActivity
import com.dicoding.mystorysubmission.utlis.ViewModelFactory
import com.dicoding.mystorysubmission.utlis.getImageUri
import com.dicoding.mystorysubmission.utlis.reduceFileImage
import com.dicoding.mystorysubmission.utlis.uriToFile

class PostStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPostStoryBinding

    private val postViewModel by viewModels<PostViewModel> {
        ViewModelFactory.getInstance(this, true)
    }

    private var currentImageUri: Uri? = null

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this@PostStoryActivity, permission_granted, Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(this@PostStoryActivity, permission_denied, Toast.LENGTH_SHORT).show()
            }
        }

    private fun allPermissionGranted() =
        ContextCompat.checkSelfPermission(
            this,
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            title = getString(R.string.post_activity_title)
            setDisplayHomeAsUpEnabled(true)
        }

        if (!allPermissionGranted()) requestPermissionLauncher.launch(REQUIRED_PERMISSION)

        setBtnAction()
    }

    private fun setBtnAction() {
        binding.apply {
            btnOpenCamera.setOnClickListener {
                startCamera()
            }
            btnOpenGallery.setOnClickListener {
                startGallery()
            }
            btnPostStory.setOnClickListener {
                postImage()
            }
        }
    }

    private fun postImage() {
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, this).reduceFileImage()
            Log.d("file", "Image:  ${imageFile.path}")
            val description = binding.etDescription.text.toString()

            postViewModel.postStory(imageFile, description).observe(this) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Success -> {
                            showLoading(false)
                            Toast.makeText(
                                this@PostStoryActivity,
                                result.data.message,
                                Toast.LENGTH_SHORT
                            ).show()
                            val intentToMain =
                                Intent(this@PostStoryActivity, MainActivity::class.java)
                            intentToMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            startActivity(intentToMain)
                        }

                        is Result.Error -> {
                            showLoading(false)
                            Toast.makeText(this@PostStoryActivity, result.error, Toast.LENGTH_SHORT)
                                .show()
                        }

                        is Result.Loading -> {
                            showLoading(true)
                        }
                    }
                }
            }
        } ?: showToast(getString(R.string.empty_image_warning))
    }

    private fun showToast(message: String) {
        Toast.makeText(this@PostStoryActivity, message, Toast.LENGTH_SHORT).show()
    }


    private val launcherGallery =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
            if (uri != null) {
                currentImageUri = uri
                displayImage()
            } else {
                Log.d("photo picker", "No Media Selected")
            }
        }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherIntentCamera =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            if (isSuccess) displayImage()
        }

    private fun displayImage() {
        currentImageUri?.let {
            Log.d("Image URI", "displayImage: $it ")
            binding.ivGambar.setImageURI(it)
        }
    }

    private fun startCamera() {
        currentImageUri = getImageUri(this)
        launcherIntentCamera.launch(currentImageUri)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.apply {
            if (isLoading) {
                btnPostStory.text = " "
                pbPost.visibility = View.VISIBLE
            } else {
                btnPostStory.text = getString(R.string.post)
                pbPost.visibility = View.GONE
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
        private const val permission_granted = "Permission Granted"
        private const val permission_denied = "Permission Denied"
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}