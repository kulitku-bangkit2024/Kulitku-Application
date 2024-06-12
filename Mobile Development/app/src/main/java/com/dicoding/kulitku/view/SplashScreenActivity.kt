package com.dicoding.kulitku.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.ViewModelProvider
import com.dicoding.kulitku.MainActivity
import com.dicoding.kulitku.R
import com.dicoding.kulitku.view.login.dataStore

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        supportActionBar?.hide()

        Handler(Looper.getMainLooper()).postDelayed({
            val pref = UserPreferences.getInstance(dataStore)
            val userViewModel =
                ViewModelProvider(this, UserModelFactory(pref))[UserViewModel::class.java]

            userViewModel.getLogin().observe(this) { sessionTrue ->
                if (sessionTrue) {
                    // jika sebelumnya sudah login maka akan ke MainActivity
                    val intent = Intent(this@SplashScreenActivity, MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    startActivity(intent)
                } else {
                    // jika belum login akan diarahkan ke OnBoarding
                    val intent = Intent(this@SplashScreenActivity, OnBoardingActivity::class.java)
                    startActivity(intent)
                }
                finish()
            }
        }, SPLASH_SCREEN_DELAY)
    }

    companion object {
        private const val SPLASH_SCREEN_DELAY: Long = 2000
    }
}