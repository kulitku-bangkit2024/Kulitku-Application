package com.dicoding.kulitku.view.quiz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dicoding.kulitku.MainActivity
import com.dicoding.kulitku.R
import com.dicoding.kulitku.databinding.ActivityQuizMainBinding
import com.dicoding.kulitku.databinding.FragmentHomeBinding

class QuizMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuizMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.btnStartQuiz.setOnClickListener {
            val intent = Intent(this, QuizPageActivity::class.java)
            startActivity(intent)
        }

//        binding.btnBack.setOnClickListener {
//            val intent = Intent(this, FragmentHomeBinding::class.java)
//            startActivity(intent)
//        }

        binding.btnBack.setOnClickListener {
            // Menyiapkan intent dengan data atau flag untuk diterima oleh FragmentHome
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("backToFragmentHome", true)
            startActivity(intent)
            finish() // Optional, tergantung pada kebutuhan aplikasi
        }

    }
}