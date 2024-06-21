package com.dicoding.kulitku.view.tips

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.kulitku.api.ResponseTipsItem
import com.dicoding.kulitku.repository.Repository

class TipsViewModel(private val repository: Repository) : ViewModel() {

    val tips: LiveData<List<ResponseTipsItem>> = repository.tips
    val isLoading: LiveData<Boolean> = repository.isLoading
    val message: LiveData<String> = repository.message

    init {
        getTips()
    }

    private fun getTips() {
        repository.getTips()
    }

}