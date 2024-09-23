package com.example.fantasyaudiobooks.data.model

data class BookSeries(
    val id: Int,
    val title: String,
    val books: List<Book>
)
