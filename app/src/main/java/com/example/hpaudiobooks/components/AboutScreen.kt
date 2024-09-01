package com.example.hpaudiobooks.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun AboutScreen(navController: NavController) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column {
            Text(
                text = "About HPAudioBooks",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Text(
                text = "HPAudioBooks is an open-source app for listening to Harry Potter audiobooks.",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // GitHub Repository Link
            Text(
                text = "GitHub Repository",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Blue, //blue
                modifier = Modifier.clickable {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/Daniel6702/HPAudioBooks"))
                    context.startActivity(intent)
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Buy Me a Coffee Link
            Text(
                text = "Buy Me a Coffee",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Blue,
                modifier = Modifier.clickable {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.buymeacoffee.com"))
                    context.startActivity(intent)
                }
            )
        }

        Button(onClick = { navController.popBackStack() }) {
            Text(text = "Back")
        }
    }
}
