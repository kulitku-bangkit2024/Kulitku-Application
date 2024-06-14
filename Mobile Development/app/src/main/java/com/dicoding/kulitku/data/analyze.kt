package com.dicoding.kulitku.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class Analyze(

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,

    @ColumnInfo(name = "imageUri")
    var uri: String,

    @ColumnInfo(name  = "type")
    var type: String? = null,

    @ColumnInfo(name = "confidence")
    var confidence: Float = 0.0F
) : Parcelable