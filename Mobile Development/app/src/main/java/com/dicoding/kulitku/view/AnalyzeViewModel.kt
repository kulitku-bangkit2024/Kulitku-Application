package com.dicoding.kulitku.view

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.kulitku.data.Analyze
import com.dicoding.kulitku.data.AnalyzeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AnalyzeViewModel(application: Application) : ViewModel() {
    private val mAnalyzeRepository: AnalyzeRepository = AnalyzeRepository(application)

    val allNotes: LiveData<List<Analyze>> = mAnalyzeRepository.getAllNotes()

    fun insert(analyze: Analyze) {
        viewModelScope.launch(Dispatchers.IO) {
            mAnalyzeRepository.insert(analyze)
        }
    }
}