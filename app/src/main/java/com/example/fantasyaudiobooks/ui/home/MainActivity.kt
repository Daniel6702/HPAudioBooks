package com.example.fantasyaudiobooks.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.fantasyaudiobooks.ui.bookdetails.BookDetailsActivity
import com.example.fantasyaudiobooks.ui.booklist.BookSeriesActivity
import com.example.fantasyaudiobooks.data.model.BookSeries
import com.example.fantasyaudiobooks.data.repository.BookRepository
import com.example.fantasyaudiobooks.ui.baselayout.ScaffoldWithDrawer
import com.example.fantasyaudiobooks.ui.theme.FantasyAudiobooksTheme

class MainActivity : ComponentActivity() {

    private lateinit var bookRepository: BookRepository
    private lateinit var bookSeriesList: List<BookSeries>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bookRepository = BookRepository(this)
        bookSeriesList = bookRepository.getBookSeries()

        setContent {
            FantasyAudiobooksTheme {
                ScaffoldWithDrawer(
                    bookSeriesList = bookSeriesList,
                    onSeriesClick = { seriesId ->
                        val intent = Intent(this@MainActivity, BookSeriesActivity::class.java)
                        intent.putExtra("seriesId", seriesId)
                        startActivity(intent)
                    }
                ) { paddingValues ->
                    HomeScreen(
                        bookSeriesList = bookSeriesList,
                        paddingValues = paddingValues,
                        context = this@MainActivity,
                        onBookClick = { book ->
                            val intent = Intent(this@MainActivity, BookDetailsActivity::class.java)
                            intent.putExtra("selectedBook", book)
                            startActivity(intent)
                        }
                    )
                }
            }
        }
    }
}