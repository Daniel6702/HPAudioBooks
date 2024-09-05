package com.example.hpaudiobooks

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.hpaudiobooks.models.AudioBook

@Composable
fun DrawerContent(books: List<AudioBook>, onBookSelected: (Int) -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Books",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(16.dp)
        )

        LazyColumn {
            itemsIndexed(books) { index, book ->
                TextButton(
                    onClick = { onBookSelected(index) },
                    modifier = Modifier.fillMaxWidth().padding(8.dp)
                ) {
                    Text(text = book.bookData.name)
                }
            }
        }
    }
}
