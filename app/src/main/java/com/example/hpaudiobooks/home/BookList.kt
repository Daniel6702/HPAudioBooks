package com.example.hpaudiobooks.home
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.hpaudiobooks.models.AudioBook

@Composable
fun BookList(books: List<AudioBook>, navController: NavController) {
    LazyColumn {
        items(books) { audioBook ->
            AudiobookCard(
                coverImageId = audioBook.coverImageId,
                name = audioBook.bookData.name,
                author = audioBook.bookData.author,
                releaseYear = audioBook.bookData.releaseYear,
                lengthInMinutes = audioBook.bookData.lengthInMinutes,
                narrator = audioBook.bookData.narrator,
                navController = navController,
                modifier = Modifier.padding(8.dp),
                chapters = audioBook.chapters // Pass the chapters list
            )
        }
    }
}
