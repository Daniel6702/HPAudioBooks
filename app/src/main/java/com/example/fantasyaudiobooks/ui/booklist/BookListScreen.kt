package com.example.fantasyaudiobooks.ui.booklist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.ui.unit.dp
import com.example.fantasyaudiobooks.data.model.BookSeries
import com.example.fantasyaudiobooks.ui.common.BookListItem
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import com.example.fantasyaudiobooks.data.model.Book

@Composable
fun BookListScreen(
    series: BookSeries,
    paddingValues: PaddingValues,
    onBookClick: (Book) -> Unit,
    onBackClick: () -> Unit
) {
    Box(modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues) // Ensure padding is applied to the entire Box
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Text(
                text = series.title,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                textAlign = TextAlign.Center
            )

            Divider()

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(series.books) { book ->
                    BookListItem(book, onBookClick)
                }
            }
        }

        // Back Button Overlay with reordered modifiers
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .padding(16.dp) // Apply padding first
                .size(48.dp) // Then size
                .background(color = Color.Black.copy(alpha = 0.7f), shape = CircleShape) // Then background
                .align(Alignment.TopStart) // Finally, align
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}