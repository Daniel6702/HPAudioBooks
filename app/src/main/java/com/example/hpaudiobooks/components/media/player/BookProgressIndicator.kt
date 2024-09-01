package com.example.hpaudiobooks.components.media.player

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BookProgressIndicator(
    currentChapterIndex: Int,
    totalChapters: Int
) {
    val progress = (currentChapterIndex + 1).toFloat() / totalChapters.toFloat()
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Book Progress: ${(progress * 100).toInt()}%")
        LinearProgressIndicator(
            progress = progress,
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        )
    }
}