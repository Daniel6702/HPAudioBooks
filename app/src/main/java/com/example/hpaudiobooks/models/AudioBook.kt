package com.example.hpaudiobooks.models

data class AudioBook(
    val bookData: BookData,
    val coverImageId: Int,
    val chapters: List<Chapter>
)
