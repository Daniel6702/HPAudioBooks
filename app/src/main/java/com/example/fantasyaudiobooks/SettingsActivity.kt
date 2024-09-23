package com.example.fantasyaudiobooks

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.fantasyaudiobooks.data.model.BookSeries
import com.example.fantasyaudiobooks.data.repository.BookRepository
import com.example.fantasyaudiobooks.ui.components.ScaffoldWithDrawer
import com.example.fantasyaudiobooks.ui.screens.SettingScreen
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
