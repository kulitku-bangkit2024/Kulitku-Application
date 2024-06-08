package com.dicoding.kulitku.view

import android.app.Application
import androidx.lifecycle.ViewModel
import com.dicoding.kulitku.data.Analyze
import com.dicoding.kulitku.data.AnalyzeRepository

class AnalyzeViewModel(application: Application) : ViewModel() {
    private val mAnalyzeRepository: AnalyzeRepository = AnalyzeRepository(application)

    fun insert(analyze: Analyze) {
        mAnalyzeRepository.insert(analyze)
    }
}