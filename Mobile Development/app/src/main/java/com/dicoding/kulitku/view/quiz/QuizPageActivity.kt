package com.dicoding.kulitku.view.quiz

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dicoding.kulitku.api.ResponseQuizItem
import com.dicoding.kulitku.databinding.ActivityQuizPageBinding
import com.dicoding.kulitku.view.MainViewModelFactory

class QuizPageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuizPageBinding
    private val quizViewModel: QuizViewModel by lazy {
        ViewModelProvider(this, MainViewModelFactory(this))[QuizViewModel::class.java]
    }
    private var currentQuestionIndex = 0
    private var quizList: List<ResponseQuizItem>? = null
    private var userAnswers: MutableList<String> = mutableListOf()
    private var userScore = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        quizViewModel.quizzes.observe(this, Observer { quizzes ->
            quizzes?.let {
                quizList = it
                loadQuestion()
            }
        })

        quizViewModel.isLoading.observe(this, Observer { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        })

        quizViewModel.message.observe(this, Observer { message ->
            message?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            }
        })

        binding.button3.setOnClickListener {
            nextQuestion()
        }
    }

    private fun loadQuestion() {
        quizList?.let {
            if (currentQuestionIndex < it.size) {
                val currentQuiz = it[currentQuestionIndex]
                binding.tvQuestion.text = currentQuiz.question
                binding.radioButton1.text = currentQuiz.optionA
                binding.radioButton2.text = currentQuiz.optionB
                binding.radioButton3.text = currentQuiz.optionC
                binding.radioButton4.text = currentQuiz.optionD
                binding.totalMarks.text = "${currentQuestionIndex + 1}/${it.size}"
                clearRadioGroup()
                if (currentQuestionIndex == it.size - 1) {
                    binding.button3.text = "Lihat Skor"
                } else {
                    binding.button3.text = "Next Question"
                }
            }
        }
    }

    private fun nextQuestion() {
        quizList?.let {
            val selectedOptionId = binding.answersGroup.checkedRadioButtonId
            if (selectedOptionId != -1) {
                val selectedRadioButton = findViewById<RadioButton>(selectedOptionId)
                val selectedAnswer = selectedRadioButton.text.toString()
                userAnswers.add(selectedAnswer)

                if (currentQuestionIndex < it.size - 1) {
                    currentQuestionIndex++
                    loadQuestion()
                } else {
                    checkAnswers()
                }
            } else {
                Toast.makeText(this, "Harap pilih jawaban terlebih dahulu", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun clearRadioGroup() {
        binding.answersGroup.clearCheck()
    }

    private fun checkAnswers() {
        quizList?.let {
            for ((index, answer) in userAnswers.withIndex()) {
                val currentQuiz = it[index]
                Log.d("QuizPageActivity", "User answer: $answer, Correct answer: ${currentQuiz.answer}")
                // Comparing user's answer with the correct answer
                if (answer == currentQuiz.answer) {
                    userScore++
                }
            }
            showQuizScore()
        }
    }

    private fun showQuizScore() {
        val intent = Intent(this, QuizScoreActivity::class.java)
        intent.putExtra("userScore", userScore)
        intent.putExtra("totalQuestions", quizList?.size ?: 0)
        startActivity(intent)
        finish()
    }
}