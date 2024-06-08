package com.dicoding.kulitku.data

data class HistoryItem(
    val imageUri: String,
    val label: String,
    val date: Long
)

val sampleHistoryItems = listOf(
    HistoryItem(
        imageUri = "https://example.com/image1.jpg",
        label = "History Item 1",
        date = System.currentTimeMillis()
    ),
    HistoryItem(
        imageUri = "https://example.com/image2.jpg",
        label = "History Item 2",
        date = System.currentTimeMillis() - 86400000L // 1 day ago
    ),
    HistoryItem(
        imageUri = "https://example.com/image3.jpg",
        label = "History Item 3",
        date = System.currentTimeMillis() - 172800000L // 2 days ago
    )
)

