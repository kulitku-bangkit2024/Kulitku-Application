package com.dicoding.kulitku.view.quiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dicoding.kulitku.databinding.ActivityQuizScoreBinding

class QuizScoreActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuizScoreBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizScoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userScore = intent.getIntExtra("userScore", 0)
        val totalQuestions = intent.getIntExtra("totalQuestions", 0)

        val scorePercentage = (userScore.toFloat() / totalQuestions.toFloat()) * 100
        val scoreText = "Skor Anda: $userScore/$totalQuestions\nPersentase: ${scorePercentage.toInt()}%"

        binding.scoreTextView.text = scoreText

        binding.playAgainButton.setOnClickListener {
            finish()
        }

        binding.exitButton.setOnClickListener {
            finishAffinity()
        }
    }
}