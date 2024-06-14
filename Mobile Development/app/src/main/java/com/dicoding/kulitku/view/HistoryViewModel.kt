package com.dicoding.kulitku.view

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.kulitku.data.Analyze
import com.dicoding.kulitku.data.AnalyzeRepository

class HistoryViewModel(application: Application) : ViewModel() {
    private val mAnalyzeRepository: AnalyzeRepository = AnalyzeRepository(application)
    val allNotes: LiveData<List<Analyze>> = mAnalyzeRepository.getAllNotes()
}