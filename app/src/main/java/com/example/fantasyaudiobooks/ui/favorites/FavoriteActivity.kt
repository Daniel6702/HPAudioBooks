package com.example.fantasyaudiobooks.ui.favorites

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.fantasyaudiobooks.ui.bookdetails.BookDetailsActivity
import com.example.fantasyaudiobooks.ui.booklist.BookSeriesActivity
import com.example.fantasyaudiobooks.data.model.Book
import com.example.fantasyaudiobooks.data.repository.BookRepository
import com.example.fantasyaudiobooks.ui.baselayout.ScaffoldWithDrawer
import com.example.fantasyaudiobooks.ui.theme.FantasyAudiobooksTheme

class FavoriteActivity : ComponentActivity() {

    private lateinit var bookRepository: BookRepository
    private lateinit var favoriteBooks: List<Book>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bookRepository = BookRepository(this)

        // Retrieve the list of favorite books from SharedPreferences
        val sharedPreferences = getSharedPreferences("favorites_prefs", Context.MODE_PRIVATE)
        val favoriteBookTitles = sharedPreferences.getStringSet("favorites", emptySet()) ?: emptySet()

        // Map the book titles to actual book objects
        favoriteBooks = bookRepository.getBookSeries()
            .flatMap { it.books } // Flatten all books from all series
            .filter { favoriteBookTitles.contains(it.name) } // Filter only those in favorites

        setContent {
            FantasyAudiobooksTheme {
                ScaffoldWithDrawer(
                    bookSeriesList = bookRepository.getBookSeries(),
                    onSeriesClick = { seriesId ->
                        // Handle series click and navigate to BookSeriesActivity
                        val intent = Intent(this@FavoriteActivity, BookSeriesActivity::class.java)
                        intent.putExtra("seriesId", seriesId)
                        startActivity(intent)
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