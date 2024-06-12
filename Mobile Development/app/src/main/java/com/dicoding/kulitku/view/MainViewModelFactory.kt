package com.dicoding.kulitku.view

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.kulitku.di.Injection
import com.dicoding.kulitku.view.login.LoginViewModel
import com.dicoding.kulitku.view.register.RegisterViewModel

class MainViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return RegisterViewModel(Injection.provideRepository(context)) as T
        }

        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(Injection.provideRepository(context)) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}