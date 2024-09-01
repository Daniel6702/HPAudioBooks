package com.example.hpaudiobooks.components.media.player

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PlaybackProgressBar(
    currentPosition: Int,
    totalDuration: Int,
    onSeek: (Float) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Current Position
        Text(text = String.format("%02d:%02d", currentPosition / 60000, (currentPosition / 1000) % 60))

        Slider(
            value = currentPosition.toFloat(),
            onValueChange = onSeek,
            valueRange = 0f..totalDuration.toFloat(),
            modifier = Modifier.weight(1f).padding(horizontal = 8.dp)
        )

        // Total Duration
        Text(text = String.format("%02d:%02d", totalDuration / 60000, (totalDuration / 1000) % 60))
    }
}
