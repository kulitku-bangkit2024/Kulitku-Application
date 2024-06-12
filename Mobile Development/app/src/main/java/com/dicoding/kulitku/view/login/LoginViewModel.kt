package com.dicoding.kulitku.view.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.kulitku.api.LoginData
import com.dicoding.kulitku.api.LoginResponse
import com.dicoding.kulitku.repository.Repository

class LoginViewModel(private val repository: Repository): ViewModel() {
    val message: LiveData<String> = repository.message
    val userLogin: LiveData<LoginResponse> = repository.userLogin

    fun login(loginData: LoginData) {
        repository.loginUser(loginData)
    }

//    fun loginWithGoogle(idToken: String) {
//        repository.loginWithGoogle(idToken)
//    }
}