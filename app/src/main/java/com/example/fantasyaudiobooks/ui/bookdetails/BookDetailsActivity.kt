package com.example.fantasyaudiobooks.ui.bookdetails

import android.content.Intent
import android.os.Bundle
import com.example.fantasyaudiobooks.ui.booklist.BookSeriesActivity
import com.example.fantasyaudiobooks.data.model.Book
import com.example.fantasyaudiobooks.ui.common.BaseActivity
import com.example.fantasyaudiobooks.ui.mediaplayer.MediaPlayerActivity

class BookDetailsActivity : BaseActivity() {

    private lateinit var selectedBook: Book

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        selectedBook = intent.getParcelableExtra("selectedBook")!!

        setBaseContent { paddingValues ->
            BookDetailsScreen(
                book = selectedBook,
                bookSeriesList = bookSeriesList,
                paddingValues = paddingValues,
                onSeriesClick = { seriesId ->
                    val intent = Intent(this, BookSeriesActivity::class.java)
                    intent.putExtra("seriesId", seriesId)
                    startActivity(intent)
                    finish()
                },
                onListenClick = {
                    val intent = Intent(this, MediaPlayerActivity::class.java)
                    intent.putExtra("selectedBook", selectedBook)
                    startActivity(intent)
                }
            )
        }
    }
}