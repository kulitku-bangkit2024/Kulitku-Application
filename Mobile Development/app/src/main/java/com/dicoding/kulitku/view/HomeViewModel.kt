package com.dicoding.kulitku.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData

class HomeViewModel(userPreferences: UserPreferences) : ViewModel() {

    val userName: LiveData<String> = userPreferences.getName().asLiveData()
}