package com.dicoding.kulitku.di

import android.content.Context
import com.dicoding.kulitku.api.ApiConfigAuth
import com.dicoding.kulitku.repository.Repository

object Injection {
    fun provideRepository(context: Context): Repository {
        val apiServiceAuth = ApiConfigAuth.getApiService()
        return Repository(apiServiceAuth)
    }
}