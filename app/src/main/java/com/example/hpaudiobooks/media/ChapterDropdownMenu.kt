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
import com.example.hpaudiobooks.models.Chapter

@Composable
fun ChapterDropdownMenu(
    chapters: List<Chapter>,
    selectedChapterIndex: Int,
    onChapterSelected: (Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxWidth()) {
        Button(onClick = { expanded = true }, modifier = Modifier.fillMaxWidth()) {
            Text(text = chapters[selectedChapterIndex].title)
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            chapters.forEachIndexed { index, chapter ->
                DropdownMenuItem(
                    text = { Text(text = chapter.title) },
                    onClick = {
                        expanded = false
                        onChapterSelected(index)
                    }
                )
            }
        }
    }
}
