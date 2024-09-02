package com.example.hpaudiobooks.media

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Composable
fun SleepTimerControl(
    onTimeSelected: (Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val availableTimes = listOf(15, 30, 60) // in minutes
    val timeLabels = listOf("15 min", "30 min", "60 min")

    Box(modifier = Modifier.fillMaxWidth()) {
        Button(onClick = { expanded = true }, modifier = Modifier.fillMaxWidth()) {
            Text(text = "Set Sleep Timer")
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            availableTimes.forEachIndexed { index, time ->
                DropdownMenuItem(
                    text = { Text(text = timeLabels[index]) },
                    onClick = {
                        expanded = false
                        onTimeSelected(time)
                    }
                )
            }
        }
    }
}