package com.dicoding.kulitku.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.kulitku.data.Analyze
import com.dicoding.kulitku.repository.Repository

class HistoryViewModel(repository: Repository) : ViewModel() {
    val allNotes: LiveData<List<Analyze>> = repository.getAllAnalyze()
}