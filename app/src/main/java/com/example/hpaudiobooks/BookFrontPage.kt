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
    // Parse colors from the strings in audioBook.bookData
    val primaryColor = Color(android.graphics.Color.parseColor(audioBook.bookData.primary))
    val secondaryColor = Color(android.graphics.Color.parseColor(audioBook.bookData.secondary))
    val tertiaryColor = Color(android.graphics.Color.parseColor(audioBook.bookData.tertiary))
    val quaternaryColor = Color(android.graphics.Color.parseColor(audioBook.bookData.quaternary))

    // Background with a gradient using the quaternary and tertiary colors
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(quaternaryColor, tertiaryColor)
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

            // Book Title and Author with different styles using primary and secondary colors
            Text(
                text = audioBook.bookData.name,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                color = primaryColor // Title in primary color
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "by ${audioBook.bookData.author}",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                color = secondaryColor, // Author in secondary color
                fontStyle = FontStyle.Italic
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Book Info with icons using secondary color
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.Mic, contentDescription = "Narrator", tint = secondaryColor)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Narrator: ${audioBook.bookData.narrator}", style = MaterialTheme.typography.bodyLarge)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.CalendarToday, contentDescription = "Year", tint = secondaryColor)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Year: ${audioBook.bookData.releaseYear}", style = MaterialTheme.typography.bodyLarge)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.Timer, contentDescription = "Length", tint = secondaryColor)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Length: ${formatLength(audioBook.bookData.lengthInMinutes)}", style = MaterialTheme.typography.bodyLarge)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Book Description with scrollable text and border using primary color
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
                    .background(tertiaryColor, shape = RoundedCornerShape(8.dp)) // Background with tertiary color
                    .border(
                        width = 2.dp,
                        color = primaryColor, // Border with primary color
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(8.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(text = audioBook.bookData.description, style = MaterialTheme.typography.bodyMedium)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Listen Button with icon and modern look using primary color
            Button(
                onClick = onListenClick,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth(0.6f)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = primaryColor, // Button color from primary
                    contentColor = Color.White // Text/icon remains white
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