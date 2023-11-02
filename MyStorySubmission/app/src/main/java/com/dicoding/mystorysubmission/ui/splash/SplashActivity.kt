package com.dicoding.mystorysubmission.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.activity.viewModels
import com.dicoding.mystorysubmission.R
import com.dicoding.mystorysubmission.ui.landingScreen.LandingActivity
import com.dicoding.mystorysubmission.ui.main.MainActivity
import com.dicoding.mystorysubmission.utlis.ViewModelFactory

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private val splashViewModel by viewModels<SplashViewModel> {
        ViewModelFactory.getInstance(this,true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val handler = Handler(mainLooper)
        handler.postDelayed({
            splashViewModel.getSession().observe(this) { session ->
                intent = if (!session.isLogin) {
                    Intent(this@SplashActivity, LandingActivity::class.java)
                } else {
                    Intent(this@SplashActivity, MainActivity::class.java)
                }
                startActivity(intent)
                finish()
            }
        }, DELAY)
    }

    companion object {
        const val DELAY = 2500L
    }
}