package com.example.fantasyaudiobooks.ui.booklist

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import com.example.fantasyaudiobooks.data.model.BookSeries
import com.example.fantasyaudiobooks.ui.bookdetails.BookDetailsActivity
import com.example.fantasyaudiobooks.ui.common.BaseActivity

class BookSeriesActivity : BaseActivity() {

    private lateinit var selectedSeries: BookSeries
    private var seriesId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        seriesId = intent.getIntExtra("seriesId", -1)
        selectedSeries = bookSeriesList.find { it.id == seriesId }
            ?: throw IllegalArgumentException("Invalid series ID")

        setBaseContent { paddingValues ->
            BookListScreen(
                series = selectedSeries,
                paddingValues = paddingValues,
                onBookClick = { book ->
                    val intent = Intent(this, BookDetailsActivity::class.java).apply {
                        putExtra("selectedBook", book)
                    }
                    startActivity(intent)
                },
                onBackClick = { onBackPressedDispatcher.onBackPressed() } // Updated here
            )
        }
    }
}