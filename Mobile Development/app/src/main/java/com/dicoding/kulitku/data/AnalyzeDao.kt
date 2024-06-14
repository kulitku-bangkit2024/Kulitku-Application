package com.dicoding.kulitku.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AnalyzeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(analyze: Analyze)

    @Query("SELECT * FROM `analyze` ORDER BY id ASC")
    fun getAll() : LiveData<List<Analyze>>


}