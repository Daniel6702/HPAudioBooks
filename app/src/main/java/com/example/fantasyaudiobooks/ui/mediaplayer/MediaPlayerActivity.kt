package com.example.fantasyaudiobooks.ui.mediaplayer

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import com.example.fantasyaudiobooks.ui.booklist.BookSeriesActivity
import com.example.fantasyaudiobooks.data.model.Book
import com.example.fantasyaudiobooks.data.model.BookSeries
import com.example.fantasyaudiobooks.data.repository.BookRepository
import com.example.fantasyaudiobooks.ui.baselayout.BaseLayout
import com.example.fantasyaudiobooks.ui.theme.FantasyAudiobooksTheme
import com.example.fantasyaudiobooks.utils.SharedPreferencesUtils

class MediaPlayerActivity : ComponentActivity() {

    private lateinit var bookRepository: BookRepository
    private lateinit var bookSeriesList: List<BookSeries>
    private lateinit var selectedBook: Book
    private val viewModel: MediaPlayerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bookRepository = BookRepository(this)
        bookSeriesList = bookRepository.getBookSeries()
        selectedBook = intent.getParcelableExtra("selectedBook")!!

        val sharedPreferencesUtils = SharedPreferencesUtils(this@MediaPlayerActivity)
        sharedPreferencesUtils.saveToRecentBooks(selectedBook.name)
        val (chapterIndex, position) = sharedPreferencesUtils.loadProgress(selectedBook.name)

        setContent {
            FantasyAudiobooksTheme {
                BaseLayout(
                    bookSeriesList = bookSeriesList,
                    onSeriesClick = { seriesId ->
                        val intent =
                            Intent(this@MediaPlayerActivity, BookSeriesActivity::class.java)

                        intent.putExtra("seriesId", seriesId)
                        startActivity(intent)
                    }
                ) { paddingValues ->
                    Box(modifier = Modifier.padding(paddingValues)) {
                        MediaPlayerScreen(
                            book = selectedBook,
                            viewModel = viewModel,
                            onBackClick = { finish() },
                            initialChapterIndex = chapterIndex,
                            initialPosition = position
                        )
                    }
                }
            }
        }

    }
}


