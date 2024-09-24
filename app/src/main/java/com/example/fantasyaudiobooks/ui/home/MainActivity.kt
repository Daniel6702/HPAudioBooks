package com.example.fantasyaudiobooks.ui.home

import android.content.Intent
import android.os.Bundle
import com.example.fantasyaudiobooks.ui.bookdetails.BookDetailsActivity
import com.example.fantasyaudiobooks.ui.common.BaseActivity

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setBaseContent { paddingValues ->
            HomeScreen(
                bookSeriesList = bookSeriesList,
                paddingValues = paddingValues,
                context = this,
                onBookClick = { book ->
                    val intent = Intent(this, BookDetailsActivity::class.java)
                    intent.putExtra("selectedBook", book)
                    startActivity(intent)
                }
            )
        }
    }
}