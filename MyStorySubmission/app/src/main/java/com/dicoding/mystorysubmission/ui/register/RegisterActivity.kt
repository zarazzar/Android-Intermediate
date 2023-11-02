package com.dicoding.mystorysubmission.ui.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.dicoding.mystorysubmission.R
import com.dicoding.mystorysubmission.databinding.ActivityRegisterBinding
import com.dicoding.mystorysubmission.ui.login.LoginActivity
import com.dicoding.mystorysubmission.utlis.ViewModelFactory
import com.dicoding.mystorysubmission.data.Result

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    private val registerViewModel by viewModels<RegistryViewModel> {
        ViewModelFactory.getInstance(this, true)
    }

    private lateinit var name: String
    private lateinit var email: String
    private lateinit var pass: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        registerAnimation()
        setBtnAction()

    }

    private fun registerAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_Y, -20f, 20f).apply {
            duration = 4000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()
        binding.apply {
            val title = ObjectAnimator.ofFloat(tvTitleRegister, View.ALPHA, 1f).setDuration(500)
            val tvName = ObjectAnimator.ofFloat(tvNameRegister, View.ALPHA, 1f).setDuration(500)
            val etName = ObjectAnimator.ofFloat(tlName, View.ALPHA, 1f).setDuration(500)
            val tvEmail = ObjectAnimator.ofFloat(tvEmail, View.ALPHA, 1f).setDuration(500)
            val etEmail = ObjectAnimator.ofFloat(tlEmail, View.ALPHA, 1f).setDuration(500)
            val tvPass = ObjectAnimator.ofFloat(tvPassword, View.ALPHA, 1f).setDuration(500)
            val etPass = ObjectAnimator.ofFloat(tlPassword, View.ALPHA, 1f).setDuration(500)
            val btnRegister = ObjectAnimator.ofFloat(btnRegister, View.ALPHA, 1f).setDuration(500)
            val tvConfirmation =
                ObjectAnimator.ofFloat(tvAlreadyLogin, View.ALPHA, 1f).setDuration(500)
            val tvToLogin = ObjectAnimator.ofFloat(tvToLogin, View.ALPHA, 1f).setDuration(500)

            val playTogether = AnimatorSet().apply {
                playTogether(tvConfirmation, tvToLogin)
            }

            AnimatorSet().apply {
                playSequentially(
                    title,
                    tvName,
                    etName,
                    tvEmail,
                    etEmail,
                    tvPass,
                    etPass,
                    btnRegister,
                    playTogether
                )
                start()
            }
        }
    }

    private fun setBtnAction() {
        binding.apply {
            btnRegister.setOnClickListener {
                register()
            }
            tvToLogin.setOnClickListener {
                val intentToLogin = Intent(this@RegisterActivity, LoginActivity::class.java)
                startActivity(intentToLogin)
                finish()
            }
        }
    }

    private fun register() {
        binding.apply {
            name = etName.text.toString()
            email = etEmail.text.toString()
            pass = etPassword.text.toString()
        }
        registerViewModel.registerUser(name, email, pass).observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Success -> {
                        showLoading(false)
                        Toast.makeText(
                            this@RegisterActivity,
                            result.data.message,
                            Toast.LENGTH_SHORT
                        ).show()
                        val intentToLogin = Intent(this@RegisterActivity, LoginActivity::class.java)
                        startActivity(intentToLogin)
                        finish()
                    }

                    is Result.Error -> {
                        showLoading(false)
                        Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
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
                btnRegister.text = " "
                progressBar.visibility = View.VISIBLE
            } else {
                btnRegister.text = getString(R.string.register)
                progressBar.visibility = View.GONE
            }
        }
    }

}