package com.example.fantasyaudiobooks.ui.common

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import com.example.fantasyaudiobooks.data.model.BookSeries
import com.example.fantasyaudiobooks.data.repository.BookRepository
import com.example.fantasyaudiobooks.ui.booklist.BookSeriesActivity
import com.example.fantasyaudiobooks.ui.common.navlayout.BaseLayout
import com.example.fantasyaudiobooks.ui.theme.FantasyAudiobooksTheme

open class BaseActivity : ComponentActivity() {

    protected lateinit var bookRepository: BookRepository
    protected lateinit var bookSeriesList: List<BookSeries>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bookRepository = BookRepository(this)
        bookSeriesList = bookRepository.getBookSeries()
    }

    protected fun setBaseContent(content: @Composable (PaddingValues) -> Unit) {
        setContent {
            FantasyAudiobooksTheme {
                BaseLayout(
                    bookSeriesList = bookSeriesList,
                    onSeriesClick = { seriesId ->
                        val intent = Intent(this, BookSeriesActivity::class.java)
                        intent.putExtra("seriesId", seriesId)
                        startActivity(intent)
                        finish()
                    }
                ) { paddingValues ->
                    content(paddingValues)
                }
            }
        }
    }
}
