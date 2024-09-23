package com.example.fantasyaudiobooks.ui.components

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.fantasyaudiobooks.data.model.Book

fun getRecentBooks(context: Context): List<String> {
    val sharedPreferences = context.getSharedPreferences("recent_books_prefs", Context.MODE_PRIVATE)
    return sharedPreferences.getStringSet("recent_books", emptySet())?.toList() ?: emptyList()
}

@Composable
fun RecentBooksList(books: List<Book>, context: Context, onBookClick: (Book) -> Unit) {
    val recentBookTitles = getRecentBooks(context)
    val recentBooks = books.filter { recentBookTitles.contains(it.name) }

    if (recentBooks.isNotEmpty()) {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(recentBooks) { book ->
                BookListItem(book = book, onBookClick = onBookClick, isCompact = true)
            }
        }
    } else {
        Text(
            text = "No recent books found",
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
    }
}