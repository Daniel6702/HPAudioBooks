package com.example.fantasyaudiobooks.ui.favorites

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.fantasyaudiobooks.ui.bookdetails.BookDetailsActivity
import com.example.fantasyaudiobooks.ui.booklist.BookSeriesActivity
import com.example.fantasyaudiobooks.data.model.Book
import com.example.fantasyaudiobooks.data.repository.BookRepository
import com.example.fantasyaudiobooks.ui.baselayout.BaseLayout
import com.example.fantasyaudiobooks.ui.theme.FantasyAudiobooksTheme
import com.example.fantasyaudiobooks.utils.SharedPreferencesUtils


class FavoriteActivity : ComponentActivity() {

    private lateinit var bookRepository: BookRepository
    private lateinit var favoriteBooks: List<Book>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bookRepository = BookRepository(this)

        favoriteBooks = SharedPreferencesUtils(this@FavoriteActivity).getFavoriteBooks()

        setContent {
            FantasyAudiobooksTheme {
                BaseLayout(
                    bookSeriesList = bookRepository.getBookSeries(),
                    onSeriesClick = { seriesId ->
                        // Handle series click and navigate to BookSeriesActivity
                        val intent = Intent(this@FavoriteActivity, BookSeriesActivity::class.java)
                        intent.putExtra("seriesId", seriesId)
                        startActivity(intent)
                        finish()
                    }
                ) { paddingValues ->
                    FavoriteBooksScreen(
                        books = favoriteBooks,
                        paddingValues = paddingValues,
                        onBookClick = { book ->
                            val intent = Intent(this@FavoriteActivity, BookDetailsActivity::class.java)
                            intent.putExtra("selectedBook", book) // Pass the selected book as Parcelable
                            startActivity(intent)
                        }
                    )
                }
            }
        }
    }
}