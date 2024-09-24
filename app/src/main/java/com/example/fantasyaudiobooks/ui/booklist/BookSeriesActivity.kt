package com.example.fantasyaudiobooks.ui.booklist

import android.content.Intent
import android.os.Bundle
import com.example.fantasyaudiobooks.data.model.BookSeries
import com.example.fantasyaudiobooks.ui.bookdetails.BookDetailsActivity
import com.example.fantasyaudiobooks.ui.common.BaseActivity

class BookSeriesActivity : BaseActivity() {

    private lateinit var selectedSeries: BookSeries

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val seriesId = intent.getIntExtra("seriesId", -1)
        selectedSeries = bookSeriesList.find { it.id == seriesId }
            ?: throw IllegalArgumentException("Invalid series ID")

        setBaseContent { paddingValues ->
            BookListScreen(
                series = selectedSeries,
                paddingValues = paddingValues,
                onBookClick = { book ->
                    val intent = Intent(this, BookDetailsActivity::class.java)
                    intent.putExtra("selectedBook", book)
                    startActivity(intent)
                }
            )
        }
    }
}