package com.dicoding.kulitku.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "analyze")
@Parcelize
data class Analyze(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "imageUri")
    var uri: String,

    @ColumnInfo(name = "type")
    var type: String? = null,

    @ColumnInfo(name = "confidence")
    var confidence: Float = 0.0F,

    @ColumnInfo(name = "medicine")
    var medicine: String? = null,

    @ColumnInfo(name = "skin_type")
    var skin_type: String? = null,

    @ColumnInfo(name = "confidence_skin_type")
    var confidence_skin_type:  Float = 0.0F,

    @ColumnInfo(name = "product")
    var product: String? = null,
) : Parcelable