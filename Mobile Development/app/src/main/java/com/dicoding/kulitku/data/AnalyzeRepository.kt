package com.dicoding.kulitku.data

import android.app.Application
import androidx.lifecycle.LiveData
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class AnalyzeRepository(application: Application) {
    private val mAnalyzeDao: AnalyzeDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = AnalyzeRoomDatabase.getDatabase(application)
        mAnalyzeDao = db.analyzeDao()
    }

    fun getAllNotes(): LiveData<List<Analyze>> = mAnalyzeDao.getAll()
    fun insert(analyze: Analyze) {
        executorService.execute { mAnalyzeDao.insert(analyze) }
    }
}