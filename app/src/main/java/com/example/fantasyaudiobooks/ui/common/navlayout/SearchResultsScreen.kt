package com.example.fantasyaudiobooks.ui.common.navlayout

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.fantasyaudiobooks.ui.bookdetails.BookDetailsActivity
import com.example.fantasyaudiobooks.data.model.BookSeries
import com.example.fantasyaudiobooks.ui.common.BookListItem


@Composable
fun SearchResultsScreen(
    searchQuery: String,
    bookSeriesList: List<BookSeries>,
    paddingValues: PaddingValues
) {
    // Flatten all books from all series
    val allBooks = bookSeriesList.flatMap { it.books }
    val context = LocalContext.current

    // Filter books based on the search query
    val filteredBooks = allBooks.filter { book ->
        book.name.contains(searchQuery, ignoreCase = true) ||
                book.author.contains(searchQuery, ignoreCase = true) ||
                book.description.contains(searchQuery, ignoreCase = true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        Text(
            text = "Search Results",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(16.dp)
        )

        if (filteredBooks.isEmpty()) {
            Text(
                text = "No results found for \"$searchQuery\"",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(16.dp)
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(items = filteredBooks) { book ->
                    BookListItem(
                        book = book,
                        onBookClick = { selectedBook ->
                            val intent = Intent(context, BookDetailsActivity::class.java)
                            intent.putExtra("selectedBook", selectedBook)
                            context.startActivity(intent)
                        }
                    )
                }
            }
        }
    }
}
