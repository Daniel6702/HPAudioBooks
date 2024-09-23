package com.example.fantasyaudiobooks.ui.screens

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fantasyaudiobooks.data.model.BookSeries
import com.example.fantasyaudiobooks.ui.components.RecentBooksList
import com.example.fantasyaudiobooks.R
import com.example.fantasyaudiobooks.data.model.Book

@Composable
fun HomeScreen(bookSeriesList: List<BookSeries>, paddingValues: PaddingValues, context: Context, onBookClick: (Book) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Divider(color = Color.Gray, thickness = 1.dp)

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Welcome to Fantasy Audiobooks",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Divider(color = Color.Gray, thickness = 1.dp)

        Spacer(modifier = Modifier.height(16.dp))

        Image(
            painter = painterResource(id = R.drawable.top_image),
            contentDescription = "Top Image",
            modifier = Modifier
                .fillMaxWidth()
                .height(350.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Horizontal Line
        Divider(color = Color.Gray, thickness = 1.dp)

        Spacer(modifier = Modifier.height(16.dp))

        // Label: Recent Books
        Text(
            text = "Recent Books",
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(start = 16.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Horizontal list of recent books
        RecentBooksList(books = bookSeriesList.flatMap { it.books }, context = context, onBookClick = onBookClick)

        Spacer(modifier = Modifier.height(16.dp))
    }
}

