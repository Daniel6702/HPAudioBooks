package com.example.hpaudiobooks.components.media.player

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
fun PlaybackSpeedControl(
    currentSpeed: Float,
    onSpeedChange: (Float) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val availableSpeeds = listOf(0.5f, 1.0f, 1.5f, 2.0f)
    val speedLabels = listOf("0.5x", "1x", "1.5x", "2x")

    Box(modifier = Modifier.fillMaxWidth()) {
        Button(onClick = { expanded = true }, modifier = Modifier.fillMaxWidth()) {
            Text(text = "Speed: ${speedLabels[availableSpeeds.indexOf(currentSpeed)]}")
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            availableSpeeds.forEachIndexed { index, speed ->
                DropdownMenuItem(
                    text = { Text(text = speedLabels[index]) },
                    onClick = {
                        expanded = false
                        onSpeedChange(speed)
                    }
                )
            }
        }
    }
}