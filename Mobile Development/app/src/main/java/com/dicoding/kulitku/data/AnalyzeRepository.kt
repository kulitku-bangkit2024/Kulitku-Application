package com.dicoding.kulitku.data

import android.app.Application
import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class AnalyzeRepository(application: Application) {
    private val mAnalyzeDao: AnalyzeDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()
    private val allNotes: LiveData<List<Analyze>>

    init {
        val db = AnalyzeRoomDatabase.getDatabase(application)
        mAnalyzeDao = db.analyzeDao()
        allNotes = mAnalyzeDao.getAll()
    }

    fun getAllNotes(): LiveData<List<Analyze>> {
        return allNotes
    }
    suspend fun insert(analyze: Analyze) {
        withContext(Dispatchers.IO) {
            mAnalyzeDao.insert(analyze)
        }
    }
}