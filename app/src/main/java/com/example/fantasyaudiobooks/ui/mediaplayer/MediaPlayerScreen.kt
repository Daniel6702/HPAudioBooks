package com.example.fantasyaudiobooks.ui.mediaplayer

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.AlertDialog
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Card
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.ExposedDropdownMenuBox
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.TextField
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Forward10
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Replay10
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.fantasyaudiobooks.data.model.Book
import com.example.fantasyaudiobooks.utils.formatTime

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MediaPlayerScreen(book: Book, viewModel: MediaPlayerViewModel, onBackClick: () -> Unit, initialChapterIndex: Int, initialPosition: Int) {

    LaunchedEffect(book) {
        viewModel.playChapter(
            bookTitle = book.name,
            chapters = book.chapters,
            index = initialChapterIndex,
            startPosition = initialPosition
        )
    }

    val isPlaying by viewModel.isPlaying.observeAsState(initial = false)
    val currentChapterIndex by viewModel.currentChapterIndex.observeAsState(initial = 0)
    val currentPosition by viewModel.currentPosition.observeAsState(initial = 0)
    val duration by viewModel.duration.observeAsState(initial = 0)
    val isLoading by viewModel.isLoading.observeAsState(initial = false)

    val chapters = book.chapters

    var speedExpanded by remember { mutableStateOf(false) }
    val speeds = listOf(1.0f, 1.25f, 1.5f, 1.75f, 2.0f)
    val selectedSpeed by viewModel.playbackSpeed.observeAsState(1.0f)

    val bookProgress by viewModel.bookProgressFraction.observeAsState(0f)

    var sleepTimerDialogVisible by remember { mutableStateOf(false) }

    var chapterExpanded by remember { mutableStateOf(false) }

    fun get_length_of_current_chapter_title(): Int {
        return chapters.getOrNull(currentChapterIndex)?.name?.length ?: 0
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .size(40.dp)
                        .background(color = Color.Black.copy(alpha = 0.7f), shape = CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }

                IconButton(
                    onClick = { sleepTimerDialogVisible = true },
                    modifier = Modifier
                        .size(40.dp)
                        .background(color = Color.Black.copy(alpha = 0.7f), shape = CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Timer,
                        contentDescription = "Set Sleep Timer",
                        tint = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = book.name,
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = MaterialTheme.shapes.medium,
                elevation = 4.dp,
                backgroundColor = MaterialTheme.colorScheme.surface // Set the background color here
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = chapters.getOrNull(currentChapterIndex)?.name ?: "No Chapter",
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center
                    )

                    if (isLoading) {
                        Spacer(modifier = Modifier.height(16.dp))
                        LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                        Text(
                            text = "Loading...",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    } else {
                        Spacer(modifier = Modifier.height(16.dp))
                        Slider(
                            value = currentPosition.toFloat().takeIf { !it.isNaN() } ?: 0f,
                            onValueChange = { viewModel.seekTo(it.toInt()) },
                            valueRange = 0f..(duration.toFloat().takeIf { !it.isNaN() } ?: 1f),
                            colors = SliderDefaults.colors(
                                thumbColor = MaterialTheme.colorScheme.primary,
                                activeTrackColor = MaterialTheme.colorScheme.primary
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(formatTime(currentPosition), style = MaterialTheme.typography.bodySmall)
                            Text(formatTime(duration), style = MaterialTheme.typography.bodySmall)
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            IconButton(onClick = { viewModel.skipBackward(10) }) {
                                Icon(
                                    imageVector = Icons.Default.Replay10,
                                    contentDescription = "Skip Backward 10 seconds"
                                )
                            }

                            IconButton(onClick = {
                                val newIndex = if (currentChapterIndex > 0) currentChapterIndex - 1 else 0
                                viewModel.playChapter(book.name, chapters, newIndex)
                            }) {
                                Icon(
                                    imageVector = Icons.Default.SkipPrevious,
                                    contentDescription = "Previous"
                                )
                            }

                            FloatingActionButton(
                                onClick = {
                                    if (isPlaying) viewModel.pause() else {
                                        if (currentPosition > 0) viewModel.play() else viewModel.playChapter(
                                            book.name,
                                            chapters,
                                            currentChapterIndex
                                        )
                                    }
                                },
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            ) {
                                Icon(
                                    imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                    contentDescription = "Play/Pause"
                                )
                            }

                            IconButton(onClick = {
                                val newIndex = if (currentChapterIndex < chapters.size - 1) currentChapterIndex + 1 else currentChapterIndex
                                viewModel.playChapter(book.name, chapters, newIndex)
                            }) {
                                Icon(
                                    imageVector = Icons.Default.SkipNext,
                                    contentDescription = "Next"
                                )
                            }

                            IconButton(onClick = { viewModel.skipForward(10) }) {
                                Icon(
                                    imageVector = Icons.Default.Forward10,
                                    contentDescription = "Skip Forward 10 seconds"
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Labels above the dropdown menus
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    val chapterTitleLength = get_length_of_current_chapter_title()
                    val spacesToAdd = if (chapterTitleLength > 5) chapterTitleLength*2f else 0
                    val paddedText = "${selectedSpeed}x" + " ".repeat(spacesToAdd.toInt()) + "\u200B"

                    Text("Speed", style = MaterialTheme.typography.bodyMedium)
                    ExposedDropdownMenuBox(
                        expanded = speedExpanded,
                        onExpandedChange = { speedExpanded = !speedExpanded },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(IntrinsicSize.Min) // Ensures consistent height
                    ) {
                        TextField(
                            readOnly = true,
                            value = paddedText,
                            onValueChange = {},
                            trailingIcon = {
                                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                            },
                            colors = TextFieldDefaults.textFieldColors(
                                backgroundColor = MaterialTheme.colorScheme.surface,
                                focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                                textColor = MaterialTheme.colorScheme.onSurface
                            ),
                            modifier = Modifier.fillMaxSize() // Fills the available height
                        )
                        ExposedDropdownMenu(
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.surface),
                            expanded = speedExpanded,
                            onDismissRequest = { speedExpanded = false }
                        ) {
                            speeds.forEach { speed ->
                                DropdownMenuItem(onClick = {
                                    viewModel.setPlaybackSpeed(speed)
                                    speedExpanded = false
                                }) {
                                    Text(text = "${speed}x")
                                }
                            }
                        }
                    }
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text("Select Chapter", style = MaterialTheme.typography.bodyMedium)
                    ExposedDropdownMenuBox(
                        expanded = chapterExpanded,
                        onExpandedChange = { chapterExpanded = !chapterExpanded },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(IntrinsicSize.Min) // Ensures consistent height
                    ) {
                        TextField(
                            readOnly = true,
                            value = chapters.getOrNull(currentChapterIndex)?.name ?: "Select Chapter",
                            onValueChange = {},
                            trailingIcon = {
                                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                            },
                            colors = TextFieldDefaults.textFieldColors(
                                backgroundColor = MaterialTheme.colorScheme.surface,
                                focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                                textColor = MaterialTheme.colorScheme.onSurface
                            ),
                            modifier = Modifier.fillMaxSize() // Fills the available height
                        )
                        ExposedDropdownMenu(
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.surface),
                            expanded = chapterExpanded,
                            onDismissRequest = { chapterExpanded = false },
                        ) {
                            chapters.forEachIndexed { index, chapter ->
                                DropdownMenuItem(onClick = {
                                    viewModel.playChapter(book.name, chapters, index)
                                    chapterExpanded = false
                                }) {
                                    Text(text = chapter.name)
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f)) // Pushes content above to the top

            // Align the book progress bar to the bottom
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = "Book Progress",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                LinearProgressIndicator(
                    progress = bookProgress.takeIf { !it.isNaN() } ?: 0f,
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.primary,
                )
            }

            if (sleepTimerDialogVisible) {
                AlertDialog(
                    onDismissRequest = { sleepTimerDialogVisible = false },
                    title = { Text("Set Sleep Timer") },
                    text = {
                        Column {
                            val options = listOf(15, 30, 45, 60)
                            options.forEach { minutes ->
                                Button(
                                    onClick = {
                                        viewModel.startSleepTimer(minutes)
                                        sleepTimerDialogVisible = false
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                                    ),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("$minutes minutes")
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                            Button(
                                onClick = {
                                    viewModel.cancelSleepTimer()
                                    sleepTimerDialogVisible = false
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.error,
                                    contentColor = MaterialTheme.colorScheme.onError
                                ),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Cancel Sleep Timer")
                            }
                        }
                    },
                    confirmButton = {}
                )
            }
        }
    }
}