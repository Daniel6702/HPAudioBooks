package com.example.fantasyaudiobooks.ui.favorites

import android.content.Intent
import android.os.Bundle
import com.example.fantasyaudiobooks.ui.bookdetails.BookDetailsActivity
import com.example.fantasyaudiobooks.data.model.Book
import com.example.fantasyaudiobooks.ui.common.BaseActivity
import com.example.fantasyaudiobooks.utils.SharedPreferencesUtils

class FavoriteActivity : BaseActivity() {

    private lateinit var favoriteBooks: List<Book>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        favoriteBooks = SharedPreferencesUtils(this).getFavoriteBooks()

        setBaseContent { paddingValues ->
            FavoriteBooksScreen(
                books = favoriteBooks,
                paddingValues = paddingValues,
                onBookClick = { book ->
                    val intent = Intent(this, BookDetailsActivity::class.java)
                    intent.putExtra("selectedBook", book)
                    startActivity(intent)
                }
            )
        }
    }
}