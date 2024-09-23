package com.example.fantasyaudiobooks.ui.settings


import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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

        ClearDataDialog(
            showDialog = showDialog,
            onDismiss = { showDialog = false },
            onConfirm = {
                sharedPreferencesUtils.clearAppData()
                Toast.makeText(context, "Data cleared", Toast.LENGTH_SHORT).show()
                showDialog = false
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "Liked/Recent Books and Book Progress")

    }
}
