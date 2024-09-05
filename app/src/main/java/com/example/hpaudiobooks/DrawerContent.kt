package com.example.hpaudiobooks

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.hpaudiobooks.models.AudioBook
import kotlinx.coroutines.launch

@Composable
fun DrawerContent(
    books: List<AudioBook>,
    onBookSelected: (Int) -> Unit,
    navController: NavController,
    drawerState: DrawerState // Pass drawerState to control its visibility
) {
    val scope = rememberCoroutineScope() // Coroutine scope to handle drawer closing

    Column(modifier = Modifier.fillMaxSize()) {

        // Title at the top
        Text(
            text = "Audio Books",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(16.dp)
        )

        // Divider at the top
        Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f))

        LazyColumn(modifier = Modifier.weight(1f)) {
            itemsIndexed(books) { index, book ->
                // Book item row with image and name
                TextButton(
                    onClick = {
                        onBookSelected(index)
                        scope.launch { drawerState.close() } // Close drawer after book selection
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Book image on the left
                        Image(
                            painter = painterResource(id = book.coverImageId),
                            contentDescription = "Book Cover",
                            modifier = Modifier
                                .size(50.dp)
                                .padding(end = 8.dp)
                        )
                        // Book name
                        Text(
                            text = book.bookData.name,
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        // Divider below the book list
        Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f))

        // About button at the bottom of the drawer
        Button(
            onClick = {
                navController.navigate("about_screen")
                scope.launch { drawerState.close() } // Close drawer when navigating to "About"
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = "About")
        }
    }
}


