package com.example.hpaudiobooks.models

data class BookData(
    val coverImageName: String,
    val name: String,
    val author: String,
    val releaseYear: String,
    val lengthInMinutes: Int,
    val narrator: String,
    val description: String,
    val primary: String,
    val secondary: String,
    val tertiary: String,
    val quaternary: String
)