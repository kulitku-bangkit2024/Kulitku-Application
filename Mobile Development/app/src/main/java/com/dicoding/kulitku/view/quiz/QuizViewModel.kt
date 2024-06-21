package com.dicoding.kulitku.view.quiz

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.kulitku.api.ResponseQuizItem
import com.dicoding.kulitku.repository.Repository

class QuizViewModel(private val repository: Repository) : ViewModel() {

    val quizzes: LiveData<List<ResponseQuizItem>> = repository.quizzes
    val isLoading: LiveData<Boolean> = repository.isLoading
    val message: LiveData<String> = repository.message

    init {
        getQuizzes()
    }

    fun getQuizzes() {
        repository.getQuizzes()
    }
}
