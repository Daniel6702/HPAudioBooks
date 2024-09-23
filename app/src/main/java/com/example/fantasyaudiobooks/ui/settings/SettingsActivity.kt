package com.example.fantasyaudiobooks.ui.settings

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.fantasyaudiobooks.ui.booklist.BookSeriesActivity
import com.example.fantasyaudiobooks.data.model.BookSeries
import com.example.fantasyaudiobooks.data.repository.BookRepository
import com.example.fantasyaudiobooks.ui.baselayout.ScaffoldWithDrawer
import com.example.fantasyaudiobooks.ui.theme.FantasyAudiobooksTheme

class SettingsActivity : ComponentActivity() {

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
                        // Handle series click and navigate to BookSeriesActivity
                        val intent = Intent(this@SettingsActivity, BookSeriesActivity::class.java)
                        intent.putExtra("seriesId", seriesId)
                        startActivity(intent)
                    }
                ) { paddingValues ->
                    SettingScreen(paddingValues = paddingValues)
                }
            }
        }
    }
}
