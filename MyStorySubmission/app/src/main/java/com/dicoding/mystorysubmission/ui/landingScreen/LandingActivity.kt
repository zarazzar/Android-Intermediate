package com.dicoding.mystorysubmission.ui.landingScreen

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.dicoding.mystorysubmission.databinding.ActivityLandingBinding
import com.dicoding.mystorysubmission.ui.login.LoginActivity
import com.dicoding.mystorysubmission.ui.register.RegisterActivity

class LandingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLandingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLandingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        landingAnimation()
        btnAction()
    }

    private fun btnAction() {
        binding.apply {
            btnLoginWelcome.setOnClickListener {
                startActivity(Intent(this@LandingActivity, LoginActivity::class.java))
            }
            btnRegisterWelcome.setOnClickListener {
                startActivity(Intent(this@LandingActivity, RegisterActivity::class.java))
            }
        }
    }

    private fun landingAnimation() {
        ObjectAnimator.ofFloat(binding.ivWelcome, View.TRANSLATION_X, -20f, 20f).apply {
            duration = 5000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        binding.apply {
            val title = ObjectAnimator.ofFloat(tvTitle, View.ALPHA, 1f).setDuration(500)
            val desc = ObjectAnimator.ofFloat(tvDesc, View.ALPHA, 1f).setDuration(500)
            val login = ObjectAnimator.ofFloat(btnLoginWelcome, View.ALPHA, 1f).setDuration(500)
            val register =
                ObjectAnimator.ofFloat(btnRegisterWelcome, View.ALPHA, 1f).setDuration(500)

            val playTogether = AnimatorSet().apply {
                playTogether(login, register)
            }

            AnimatorSet().apply {
                playSequentially(title, desc, playTogether)
                start()
            }
        }
    }
}