package com.dicoding.kulitku.view

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.kulitku.di.Injection
import com.dicoding.kulitku.view.article.ArticleViewModel
import com.dicoding.kulitku.view.history.HistoryViewModel
import com.dicoding.kulitku.view.login.LoginViewModel
import com.dicoding.kulitku.view.quiz.QuizViewModel
import com.dicoding.kulitku.view.register.RegisterViewModel
import com.dicoding.kulitku.view.scan.AnalyzeViewModel
import com.dicoding.kulitku.view.tips.TipsViewModel

class MainViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return RegisterViewModel(Injection.provideRepository(context)) as T
        }

        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(Injection.provideRepository(context)) as T
        }

        if (modelClass.isAssignableFrom(AnalyzeViewModel::class.java)) {
            return AnalyzeViewModel(Injection.provideRepository(context)) as T
        }

        if (modelClass.isAssignableFrom(HistoryViewModel::class.java)) {
            return HistoryViewModel(Injection.provideRepository(context)) as T
        }

        if (modelClass.isAssignableFrom(ArticleViewModel::class.java)) {
            return ArticleViewModel(Injection.provideRepository(context)) as T
        }

        if (modelClass.isAssignableFrom(TipsViewModel::class.java)) {
            return TipsViewModel(Injection.provideRepository(context)) as T
        }

        if (modelClass.isAssignableFrom(QuizViewModel::class.java)) {
            return QuizViewModel(Injection.provideRepository(context)) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}