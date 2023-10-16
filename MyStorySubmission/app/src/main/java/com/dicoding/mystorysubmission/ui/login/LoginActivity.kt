package com.dicoding.mystorysubmission.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.dicoding.mystorysubmission.R
import com.dicoding.mystorysubmission.data.Result
import com.dicoding.mystorysubmission.data.preferences.SessionModel
import com.dicoding.mystorysubmission.databinding.ActivityLoginBinding
import com.dicoding.mystorysubmission.ui.main.MainActivity
import com.dicoding.mystorysubmission.ui.register.RegisterActivity
import com.dicoding.mystorysubmission.utlis.ViewModelFactory

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private val loginViewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this,true)
    }

    private lateinit var email: String
    private lateinit var pass: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loginAnimation()
        setBtnAction()


    }

    private fun setBtnAction() {
        binding.apply {
            btnLogin.setOnClickListener {
                login()
            }
            tvToRegister.setOnClickListener {
                val intentToRegister = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(intentToRegister)
                finish()
            }
        }
    }

    private fun login() {
        binding.apply {
            email = etEmail.text.toString()
            pass = etPassword.text.toString()
        }
        loginViewModel.loginUser(email, pass).observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Success -> {
                        showLoading(false)
                        loginViewModel.saveSessionUser(
                            SessionModel(
                                result.data.loginResult.name,
                                result.data.loginResult.token
                            )
                        )
                        Toast.makeText(this@LoginActivity, result.data.message, Toast.LENGTH_SHORT).show()
                        val intentToMain = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intentToMain)
                        finish()
                    }

                    is Result.Error -> {
                        showLoading(false)
                        Toast.makeText(this@LoginActivity, result.error, Toast.LENGTH_SHORT).show()
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
                btnLogin.text = " "
                progressBar.visibility = View.VISIBLE
            } else {
                btnLogin.text = getString(R.string.login)
                progressBar.visibility = View.GONE
            }
        }
    }

    private fun loginAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -20f, 20f).apply {
            duration = 5000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()
        binding.apply {
            val title = ObjectAnimator.ofFloat(tvTitleLogin, View.ALPHA, 1f).setDuration(500)
            val desc = ObjectAnimator.ofFloat(tvDescLogin, View.ALPHA, 1f).setDuration(500)
            val tvemail = ObjectAnimator.ofFloat(tvEmail, View.ALPHA, 1f).setDuration(500)
            val etemail = ObjectAnimator.ofFloat(tlEmail, View.ALPHA, 1f).setDuration(500)
            val tvpass = ObjectAnimator.ofFloat(tvPassword, View.ALPHA, 1f).setDuration(500)
            val etpass = ObjectAnimator.ofFloat(tlPass, View.ALPHA, 1f).setDuration(500)
            val btnlogin = ObjectAnimator.ofFloat(btnLogin, View.ALPHA, 1f).setDuration(500)
            val tvconfirmation =
                ObjectAnimator.ofFloat(tvNotHaveAcc, View.ALPHA, 1f).setDuration(500)
            val tvtoregister = ObjectAnimator.ofFloat(tvToRegister, View.ALPHA, 1f).setDuration(500)

            val playTogehter = AnimatorSet().apply {
                playTogether(tvconfirmation, tvtoregister)
            }

            AnimatorSet().apply {
                playSequentially(
                    title,
                    desc,
                    tvemail,
                    etemail,
                    tvpass,
                    etpass,
                    btnlogin,
                    playTogehter
                )
            }.start()

        }
    }


}