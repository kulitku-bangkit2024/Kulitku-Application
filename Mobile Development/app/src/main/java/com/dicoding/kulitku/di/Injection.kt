package com.dicoding.kulitku.di

import android.content.Context
import com.dicoding.kulitku.api.ApiConfigAuth
import com.dicoding.kulitku.data.AnalyzeRoomDatabase
import com.dicoding.kulitku.repository.Repository

object Injection {
    fun provideRepository(context: Context): Repository {
        val apiServiceAuth = ApiConfigAuth.getApiService()
        val database = AnalyzeRoomDatabase.getDatabase(context)
        return Repository(apiServiceAuth, database)
    }
}