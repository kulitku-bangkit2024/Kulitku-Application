package com.dicoding.kulitku.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.kulitku.api.NewsItem
import com.dicoding.kulitku.data.NewsRepository

class NewsViewModel : ViewModel() {
    private val newsRepository = NewsRepository()
    private val _newsList = MutableLiveData<List<NewsItem>>()
    val newsList: LiveData<List<NewsItem>> = _newsList

    fun fetchHealthNews() {
        newsRepository.getHealthNews(
            onSuccess = { newsList ->
                _newsList.postValue(newsList)
            },
            onFailure = { errorMessage ->
            }
        )
    }
}