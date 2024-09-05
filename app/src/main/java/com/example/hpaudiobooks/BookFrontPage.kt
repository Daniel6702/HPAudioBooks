package com.example.hpaudiobooks

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.hpaudiobooks.models.AudioBook

@Composable
fun BookFrontPage(audioBook: AudioBook, onListenClick: () -> Unit) {
    // Background with a gradient
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFFe0f7fa), Color(0xFF80deea))
                )
            )
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Book Cover with rounded corners and shadow
            Image(
                painter = painterResource(id = audioBook.coverImageId),
                contentDescription = "Book Cover",
                modifier = Modifier
                    .size(200.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .align(Alignment.CenterHorizontally)
                    .shadow(8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Book Title and Author with different styles
            Text(
                text = audioBook.bookData.name,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF00695C)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "by ${audioBook.bookData.author}",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                color = Color(0xFF00796B),
                fontStyle = FontStyle.Italic
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Book Info with icons
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.Mic, contentDescription = "Narrator", tint = Color(0xFF004D40))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Narrator: ${audioBook.bookData.narrator}", style = MaterialTheme.typography.bodyLarge)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.CalendarToday, contentDescription = "Year", tint = Color(0xFF004D40))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Year: ${audioBook.bookData.releaseYear}", style = MaterialTheme.typography.bodyLarge)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.Timer, contentDescription = "Length", tint = Color(0xFF004D40))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Length: ${formatLength(audioBook.bookData.lengthInMinutes)}", style = MaterialTheme.typography.bodyLarge)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Book Description with scrollable text
            Text(
                text = "Description:",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .background(Color(0xFFE0F2F1), shape = RoundedCornerShape(8.dp))
                    .border(
                        width = 2.dp,
                        color = Color(0xFF00796B), // Dark green border color
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(8.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(text = audioBook.bookData.description, style = MaterialTheme.typography.bodyMedium)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Listen Button with icon and modern look
            Button(
                onClick = onListenClick,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth(0.6f)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF00796B), // Replaces backgroundColor
                    contentColor = Color.White // Remains the same
                ),
                shape = RoundedCornerShape(50)
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Listen",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Listen", style = MaterialTheme.typography.titleLarge)
            }
        }
    }
}

fun formatLength(lengthInMinutes: Int): String {
    val hours = lengthInMinutes / 60
    val minutes = lengthInMinutes % 60
    return if (hours > 0) {
        "$hours hr ${minutes} min"
    } else {
        "$minutes min"
    }
}