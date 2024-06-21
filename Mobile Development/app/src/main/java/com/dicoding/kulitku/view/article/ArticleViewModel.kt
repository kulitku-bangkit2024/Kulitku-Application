package com.dicoding.kulitku.view.article

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.kulitku.api.ResponseArticleItem
import com.dicoding.kulitku.repository.Repository

class ArticleViewModel(private val repository: Repository) : ViewModel() {

    val articles: LiveData<List<ResponseArticleItem>> = repository.articles
    val isLoading: LiveData<Boolean> = repository.isLoading
    val message: LiveData<String> = repository.message

    init {
        getArticles()
    }

    private fun getArticles() {
        repository.getArticles()
    }
}