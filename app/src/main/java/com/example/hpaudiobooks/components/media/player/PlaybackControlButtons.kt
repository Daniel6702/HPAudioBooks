package com.example.hpaudiobooks.components.media.player

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.FastRewind
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PlaybackControlButtons(
    isPlaying: Boolean,
    onPlayPause: () -> Unit,
    onRewind: () -> Unit,
    onFastForward: () -> Unit,
    onPreviousChapter: () -> Unit,
    onNextChapter: () -> Unit,
    isPreviousEnabled: Boolean,
    isNextEnabled: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        // Previous Chapter Button
        if (isPreviousEnabled) {
            IconButton(onClick = onPreviousChapter) {
                Icon(Icons.Default.SkipPrevious, contentDescription = "Previous Chapter")
            }
        } else {
            Spacer(modifier = Modifier.width(48.dp)) // Placeholder if no previous chapter
        }

        IconButton(onClick = onRewind) {
            Icon(Icons.Default.FastRewind, contentDescription = "Rewind 15 seconds")
        }

        IconButton(onClick = onPlayPause) {
            Icon(
                if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                contentDescription = if (isPlaying) "Pause" else "Play"
            )
        }

        IconButton(onClick = onFastForward) {
            Icon(Icons.Default.FastForward, contentDescription = "Fast Forward 15 seconds")
        }

        // Next Chapter Button
        if (isNextEnabled) {
            IconButton(onClick = onNextChapter) {
                Icon(Icons.Default.SkipNext, contentDescription = "Next Chapter")
            }
        } else {
            Spacer(modifier = Modifier.width(48.dp)) // Placeholder if no next chapter
        }
    }
}
