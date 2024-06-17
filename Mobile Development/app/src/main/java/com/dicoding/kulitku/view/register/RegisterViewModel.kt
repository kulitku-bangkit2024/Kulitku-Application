package com.dicoding.kulitku.view.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.kulitku.api.RegisterData
import com.dicoding.kulitku.repository.Repository

class RegisterViewModel(private val repository: Repository) : ViewModel() {

    val isLoading: LiveData<Boolean> = repository.isLoading
    val message: LiveData<String> = repository.message

    fun register(registerData: RegisterData) {
        repository.registerUser(registerData)
    }
}