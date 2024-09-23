package com.example.fantasyaudiobooks.ui.screens


import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

fun clearAppData(context: Context) {
    // Clear Favorites
    val favoritesPrefs = context.getSharedPreferences("favorites_prefs", Context.MODE_PRIVATE)
    favoritesPrefs.edit().clear().apply()

    // Clear Recent Books
    val recentBooksPrefs = context.getSharedPreferences("recent_books_prefs", Context.MODE_PRIVATE)
    recentBooksPrefs.edit().clear().apply()

    // Clear Book Progress
    val bookProgressPrefs = context.getSharedPreferences("book_progress_prefs", Context.MODE_PRIVATE)
    bookProgressPrefs.edit().clear().apply()
}

@Composable
fun SettingScreen(paddingValues: PaddingValues) {
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = {
                showDialog = true
            }
        ) {
            Text(text = "Clear Data")
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text(text = "Confirm Clear Data") },
                text = { Text(text = "Are you sure you want to clear all data? This action cannot be undone.") },
                confirmButton = {
                    TextButton(onClick = {
                        clearAppData(context)
                        Toast.makeText(context, "Data cleared", Toast.LENGTH_SHORT).show()
                        showDialog = false
                    }) {
                        Text("Yes")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text("No")
                    }
                }
            )
        }
        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "Liked/Recent Books and Book Progress")

    }
}
