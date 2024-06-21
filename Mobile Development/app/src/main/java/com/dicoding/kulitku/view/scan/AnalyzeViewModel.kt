package com.dicoding.kulitku.view.scan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.kulitku.data.Analyze
import com.dicoding.kulitku.repository.Repository
import kotlinx.coroutines.launch

class AnalyzeViewModel(private val repository: Repository) : ViewModel() {

    fun insertAnalyze(analyze: Analyze) {
        viewModelScope.launch {
            repository.insertAnalyze(analyze)
        }
    }

}