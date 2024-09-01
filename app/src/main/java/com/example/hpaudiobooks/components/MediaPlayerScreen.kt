package com.example.hpaudiobooks.components

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.io.IOException
import androidx.compose.material.icons.filled.FastRewind
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.example.hpaudiobooks.models.Chapter
@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun MediaPlayerScreen(
    bookName: String,
    chapterTitle: String,
    audioFilePath: String,
    coverImageId: Int,  // Add a parameter for the cover image
    onBack: () -> Unit,
    chapters: List<Chapter>,
    currentChapterIndex: Int,
    navController: NavController,
    autoPlay: Boolean = false,
    resumePosition: Int = 0  // Pass the resume position
) {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("audiobook_prefs", Context.MODE_PRIVATE)
    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }
    var isPlaying by remember { mutableStateOf(autoPlay) }
    var currentPosition by remember { mutableStateOf(resumePosition) }  // Start from the resume position
    var totalDuration by remember { mutableStateOf(0) }
    var expanded by remember { mutableStateOf(false) }
    var selectedChapterIndex by remember { mutableStateOf(currentChapterIndex) }

    DisposableEffect(Unit) {
        mediaPlayer = MediaPlayer().apply {
            try {
                val assetFileDescriptor = context.assets.openFd(audioFilePath)
                setDataSource(
                    assetFileDescriptor.fileDescriptor,
                    assetFileDescriptor.startOffset,
                    assetFileDescriptor.length
                )
                prepare()
                totalDuration = duration

                if (autoPlay || resumePosition > 0) {
                    seekTo(resumePosition)
                    if (autoPlay) {
                        start()
                    }
                }

                setOnCompletionListener {
                    if (selectedChapterIndex < chapters.size - 1) {
                        val nextChapter = chapters[selectedChapterIndex + 1]
                        val encodedPath = Uri.encode(nextChapter.path)
                        navController.navigate("media_player/$encodedPath?autoPlay=true&resume=false")
                    } else {
                        isPlaying = false
                    }
                }
            } catch (e: IOException) {
                Log.e("MediaPlayer", "Error initializing media player", e)
            }
        }

        onDispose {
            mediaPlayer?.let {
                val currentPos = it.currentPosition
                val editor = sharedPreferences.edit()
                editor.putString("${bookName}_last_chapter", chapters[selectedChapterIndex].path)
                editor.putInt("${bookName}_last_position", currentPos)
                editor.apply()
                it.release()
            }
            mediaPlayer = null
        }
    }

    LaunchedEffect(isPlaying) {
        while (mediaPlayer != null && isPlaying) {
            currentPosition = mediaPlayer?.currentPosition ?: 0
            kotlinx.coroutines.delay(1000L)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        TopAppBar(
            title = { Text(text = "") },
            navigationIcon = {
                IconButton(onClick = { navController.navigate("book_list") { popUpTo("book_list") { inclusive = true } } }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back to Book List")
                }
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Cover Image
        Image(
            painter = painterResource(id = coverImageId),
            contentDescription = "Cover Image",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(16.dp)
        )

        Text(text = bookName, style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))

        // Chapter Dropdown Menu
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
                            selectedChapterIndex = index
                            val encodedPath = Uri.encode(chapters[index].path)
                            navController.navigate("media_player/$encodedPath?autoPlay=false&resume=false")
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Current Position
            Text(text = String.format("%02d:%02d", currentPosition / 60000, (currentPosition / 1000) % 60))

            Slider(
                value = currentPosition.toFloat(),
                onValueChange = { newPosition ->
                    mediaPlayer?.seekTo(newPosition.toInt())
                },
                valueRange = 0f..totalDuration.toFloat(),
                modifier = Modifier.weight(1f).padding(horizontal = 8.dp)
            )

            // Total Duration
            Text(text = String.format("%02d:%02d", totalDuration / 60000, (totalDuration / 1000) % 60))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            // Previous Chapter Button
            if (selectedChapterIndex > 0) {
                IconButton(onClick = {
                    val previousChapter = chapters[selectedChapterIndex - 1]
                    val encodedPath = Uri.encode(previousChapter.path)
                    navController.navigate("media_player/$encodedPath?autoPlay=$isPlaying&resume=false")
                }) {
                    Icon(Icons.Default.SkipPrevious, contentDescription = "Previous Chapter")
                }
            } else {
                Spacer(modifier = Modifier.width(48.dp)) // Placeholder if no previous chapter
            }

            IconButton(onClick = {
                mediaPlayer?.seekTo((mediaPlayer?.currentPosition ?: 0) - 15000)
            }) {
                Icon(Icons.Default.FastRewind, contentDescription = "Rewind 15 seconds")
            }

            IconButton(onClick = {
                isPlaying = if (isPlaying) {
                    mediaPlayer?.pause()
                    false
                } else {
                    mediaPlayer?.start()
                    true
                }
            }) {
                Icon(
                    if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = if (isPlaying) "Pause" else "Play"
                )
            }

            IconButton(onClick = {
                mediaPlayer?.seekTo((mediaPlayer?.currentPosition ?: 0) + 15000)
            }) {
                Icon(Icons.Default.FastForward, contentDescription = "Fast Forward 15 seconds")
            }

            // Next Chapter Button
            if (selectedChapterIndex < chapters.size - 1) {
                IconButton(onClick = {
                    val nextChapter = chapters[selectedChapterIndex + 1]
                    val encodedPath = Uri.encode(nextChapter.path)
                    navController.navigate("media_player/$encodedPath?autoPlay=$isPlaying&resume=false")
                }) {
                    Icon(Icons.Default.SkipNext, contentDescription = "Next Chapter")
                }
            } else {
                Spacer(modifier = Modifier.width(48.dp)) // Placeholder if no next chapter
            }
        }
    }
}

