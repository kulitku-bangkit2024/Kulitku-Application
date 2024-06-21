package com.dicoding.kulitku.view.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dicoding.kulitku.view.UserPreferences

class HomeViewModel(userPreferences: UserPreferences) : ViewModel() {

    val userName: LiveData<String> = userPreferences.getName().asLiveData()
}