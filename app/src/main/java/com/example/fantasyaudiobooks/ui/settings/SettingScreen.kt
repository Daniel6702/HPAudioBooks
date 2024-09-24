package com.example.fantasyaudiobooks.ui.settings


import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.fantasyaudiobooks.utils.SharedPreferencesUtils

@Composable
fun SettingScreen(paddingValues: PaddingValues) {
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }
    val sharedPreferencesUtils = SharedPreferencesUtils(context)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(16.dp),
    ) {
        Text(
            text = "Settings",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            textAlign = TextAlign.Center
        )

        Divider()
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "FantasyAudioBooks is an open-source android app for listening to the greatest fantasy audiobooks ever written. These include; The Lord of the Rings, A Song of Ice and Fire, Harry Potter, The Witcher and more."
        )

        Spacer(modifier = Modifier.height(8.dp))
        Divider()
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                showDialog = true
            }
        ) {
            Text(text = "Clear Data")
        }

        ClearDataDialog(
            showDialog = showDialog,
            onDismiss = { showDialog = false },
            onConfirm = {
                sharedPreferencesUtils.clearAppData()
                Toast.makeText(context, "Data cleared", Toast.LENGTH_SHORT).show()
                showDialog = false
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Divider()
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/Daniel6702/HPAudioBooks"))
                context.startActivity(intent)
            }
        ) {
            Text(text = "GitHub Repository")
        }

        Spacer(modifier = Modifier.height(16.dp))
        Divider()
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://buymeacoffee.com/Daniel6702"))
                context.startActivity(intent)
            }
        ) {
            Text(text = "Buy Me a Coffee")
        }

        Spacer(modifier = Modifier.height(16.dp))
        Divider()
    }
}