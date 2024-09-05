package com.example.hpaudiobooks

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.hpaudiobooks.models.AudioBook

@Composable
fun BookFrontPage(audioBook: AudioBook, onListenClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Book Cover
        Image(
            painter = painterResource(id = audioBook.coverImageId),
            contentDescription = "Book Cover",
            modifier = Modifier.size(150.dp).align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Book Details
        Text(text = "Title: ${audioBook.bookData.name}", style = MaterialTheme.typography.headlineMedium)
        Text(text = "Author: ${audioBook.bookData.author}", style = MaterialTheme.typography.bodyLarge)
        Text(text = "Narrator: ${audioBook.bookData.narrator}", style = MaterialTheme.typography.bodyLarge)
        Text(text = "Year: ${audioBook.bookData.releaseYear}", style = MaterialTheme.typography.bodyLarge)
        Text(text = "Length: ${audioBook.bookData.lengthInMinutes} min", style = MaterialTheme.typography.bodyLarge)

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = onListenClick, modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text(text = "Listen")
        }
    }
}
