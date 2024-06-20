package com.dicoding.kulitku.view

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData

class ProfileViewModel(userPreferences: UserPreferences) : ViewModel() {
    val name: LiveData<String> = userPreferences.getName().asLiveData()
    val email: LiveData<String> = userPreferences.getEmail().asLiveData()

    init {
        email.observeForever { email ->
            Log.d("ProfileViewModel", "Email observed: $email")
        }
    }
}
