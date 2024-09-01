package com.example.hpaudiobooks.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.hpaudiobooks.models.AudioBook

@Composable
fun HomeScreen(navController: NavController, audioBooks: List<AudioBook>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        // Title at the top
        Text(
            text = "HPAudioBooks",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Wrap the book list in a Box to ensure it takes remaining space
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
        ) {
            BookList(books = audioBooks, navController = navController)
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Button at the bottom
        Button(
            onClick = { navController.navigate("about_screen") },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "About")
        }
    }
}

