package com.example.fantasyaudiobooks.ui.booklist

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import com.example.fantasyaudiobooks.data.model.BookSeries
import com.example.fantasyaudiobooks.data.repository.BookRepository
import com.example.fantasyaudiobooks.ui.baselayout.ScaffoldWithDrawer
import com.example.fantasyaudiobooks.ui.bookdetails.BookDetailsActivity
import com.example.fantasyaudiobooks.ui.theme.FantasyAudiobooksTheme

class BookSeriesActivity : ComponentActivity() {

    private lateinit var bookRepository: BookRepository
    private lateinit var selectedSeries: BookSeries

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val seriesId = intent.getIntExtra("seriesId", -1)
        bookRepository = BookRepository(this)
        val bookSeriesList = bookRepository.getBookSeries()
        selectedSeries = bookSeriesList.find { it.id == seriesId }
            ?: throw IllegalArgumentException("Invalid series ID")

        setContent {
            FantasyAudiobooksTheme {
                ScaffoldWithDrawer(
                    bookSeriesList = bookSeriesList,
                    onSeriesClick = { seriesId ->
                        // Navigate to the new activity for the selected series
                        val intent = Intent(this@BookSeriesActivity, BookSeriesActivity::class.java)
                        intent.putExtra("seriesId", seriesId)
                        startActivity(intent)
                    },
                    containerColor = MaterialTheme.colorScheme.primary // Or customize the color
                ) { paddingValues ->
                    BookListScreen(
                        series = selectedSeries,
                        paddingValues = paddingValues,
                        onBookClick = { book ->
                            val intent =
                                Intent(this@BookSeriesActivity, BookDetailsActivity::class.java)
                            intent.putExtra(
                                "selectedBook",
                                book
                            ) // Pass the selected book as Parcelable
                            startActivity(intent)
                        }
                    )
                }
            }
        }
    }
}