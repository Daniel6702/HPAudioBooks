package com.example.fantasyaudiobooks.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.fantasyaudiobooks.data.model.Book

@OptIn(ExperimentalCoilApi::class)
@Composable
fun BookListItem(book: Book, onBookClick: (Book) -> Unit, isCompact: Boolean = false) {
    Card(
        modifier = Modifier
            .clickable { onBookClick(book) }
            .padding(8.dp)
            .then(if (isCompact) Modifier.width(150.dp) else Modifier.fillMaxWidth())
    ) {
        if (isCompact) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(8.dp)
            ) {
                Image(
                    painter = rememberImagePainter(data = "file:///android_asset/cover_images/${book.coverImageName}.jpg"),
                    contentDescription = book.name,
                    modifier = Modifier
                        .size(120.dp)
                        .clip(RoundedCornerShape(8.dp))
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = book.name,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    maxLines = 2
                )
            }
        } else {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Book image
                Image(
                    painter = rememberImagePainter(data = "file:///android_asset/cover_images/${book.coverImageName}.jpg"),
                    contentDescription = book.name,
                    modifier = Modifier.size(80.dp)
                )

                Spacer(modifier = Modifier.width(16.dp))

                // Book details
                Column {
                    Text(text = book.name, style = MaterialTheme.typography.titleMedium)
                    Text(text = "Author: ${book.author}", style = MaterialTheme.typography.bodyMedium)
                    Text(text = "Year: ${book.releaseYear}", style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}