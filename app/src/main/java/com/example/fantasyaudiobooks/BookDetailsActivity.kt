package com.example.fantasyaudiobooks

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.fantasyaudiobooks.data.model.Book
import com.example.fantasyaudiobooks.data.model.BookSeries
import com.example.fantasyaudiobooks.data.repository.BookRepository
import com.example.fantasyaudiobooks.ui.screens.BookDetailsScreen
import com.example.fantasyaudiobooks.ui.theme.FantasyAudiobooksTheme

class BookDetailsActivity : ComponentActivity() {

    private lateinit var selectedBook: Book
    private lateinit var bookSeriesList: List<BookSeries>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        selectedBook = intent.getParcelableExtra("selectedBook")!!

        bookSeriesList = BookRepository(this).getBookSeries()

        setContent {
            FantasyAudiobooksTheme {
                BookDetailsScreen(
                    book = selectedBook,
                    bookSeriesList = bookSeriesList,
                    onSeriesClick = { seriesId ->
                        val intent =
                            Intent(this@BookDetailsActivity, BookSeriesActivity::class.java)
                        intent.putExtra("seriesId", seriesId)
                        startActivity(intent)
                    },
                    onListenClick = {
                        val intent =
                            Intent(this@BookDetailsActivity, MediaPlayerActivity::class.java)
                        intent.putExtra("selectedBook", selectedBook)
                        startActivity(intent)
                    }
                )
            }
        }
    }
}