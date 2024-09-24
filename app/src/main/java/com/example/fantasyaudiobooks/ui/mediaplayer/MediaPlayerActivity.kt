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
import com.example.fantasyaudiobooks.ui.common.BaseActivity
import com.example.fantasyaudiobooks.ui.common.navlayout.BaseLayout
import com.example.fantasyaudiobooks.ui.theme.FantasyAudiobooksTheme
import com.example.fantasyaudiobooks.utils.SharedPreferencesUtils

class MediaPlayerActivity : BaseActivity() {

    private lateinit var selectedBook: Book
    private val viewModel: MediaPlayerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        selectedBook = intent.getParcelableExtra("selectedBook")!!

        val sharedPreferencesUtils = SharedPreferencesUtils(this)
        sharedPreferencesUtils.saveToRecentBooks(selectedBook.name)
        val (chapterIndex, position) = sharedPreferencesUtils.loadProgress(selectedBook.name)

        setBaseContent { paddingValues ->
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

