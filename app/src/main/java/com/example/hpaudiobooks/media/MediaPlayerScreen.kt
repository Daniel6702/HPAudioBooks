package com.example.hpaudiobooks.media

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.io.IOException
import androidx.navigation.NavController
import com.example.hpaudiobooks.models.Chapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun MediaPlayerScreen(
    bookName: String,
    chapterTitle: String,
    audioFilePath: String,
    coverImageId: Int,
    onBack: () -> Unit,
    chapters: List<Chapter>,
    currentChapterIndex: Int,
    navController: NavController,
    autoPlay: Boolean = false,
    resumePosition: Int = 0
) {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("audiobook_prefs", Context.MODE_PRIVATE)
    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }
    var isPlaying by remember { mutableStateOf(autoPlay) }
    var currentPosition by remember { mutableStateOf(resumePosition) }
    var totalDuration by remember { mutableStateOf(0) }
    var selectedChapterIndex by remember { mutableStateOf(currentChapterIndex) }
    var playbackSpeed by remember { mutableStateOf(1.0f) }  // Default speed is 1x
    var sleepTimerJob by remember { mutableStateOf<Job?>(null) } // Track the sleep timer job

    DisposableEffect(audioFilePath) {
        mediaPlayer = MediaPlayer().apply {
            try {
                val assetFileDescriptor = context.assets.openFd(audioFilePath)
                setDataSource(
                    assetFileDescriptor.fileDescriptor,
                    assetFileDescriptor.startOffset,
                    assetFileDescriptor.length
                )
                prepare()  // Prepare the media player

                totalDuration = duration
                playbackParams = playbackParams.setSpeed(playbackSpeed)

                Log.d("MediaPlayer", "autoPlay: $autoPlay isPlaying: $isPlaying resumePosition: $resumePosition")
                if (resumePosition > 0) {
                    seekTo(resumePosition)  // Seek to the resume position
                }

                if (autoPlay) {
                    isPlaying = true
                    start()  // Start playback if autoPlay is true
                } else {
                    pause()  // Ensure it's paused if autoPlay is false
                }

                setOnCompletionListener {
                    Log.d("MediaPlayer", "Playback completed")
                    if (selectedChapterIndex < chapters.size - 1) {
                        val nextChapter = chapters[selectedChapterIndex + 1]
                        val encodedPath = Uri.encode(nextChapter.path)
                        navController.navigate("media_player/$encodedPath?autoPlay=$isPlaying&resume=false")
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
            sleepTimerJob?.cancel() // Cancel any running sleep timer when leaving the screen
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
        TopAppBarWithBackButton(navController = navController)


        Spacer(modifier = Modifier.height(8.dp))

        BookCoverImage(coverImageId = coverImageId)

        Text(
            text = bookName,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.wrapContentSize(Alignment.Center)
        )
        Spacer(modifier = Modifier.height(8.dp))

        ChapterDropdownMenu(
            chapters = chapters,
            selectedChapterIndex = selectedChapterIndex,
            onChapterSelected = { index ->
                selectedChapterIndex = index
                val encodedPath = Uri.encode(chapters[index].path)
                navController.navigate("media_player/$encodedPath?autoPlay=$isPlaying&resume=false")
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        PlaybackProgressBar(
            currentPosition = currentPosition,
            totalDuration = totalDuration,
            onSeek = { newPosition -> mediaPlayer?.seekTo(newPosition.toInt()) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        PlaybackControlButtons(
            isPlaying = isPlaying,
            onPlayPause = {
                isPlaying = if (isPlaying) {
                    mediaPlayer?.pause()
                    false
                } else {
                    mediaPlayer?.start()
                    true
                }
                Log.d("MediaPlayer", "isPlaying: $isPlaying")
            },
            onRewind = { mediaPlayer?.seekTo((mediaPlayer?.currentPosition ?: 0) - 15000) },
            onFastForward = { mediaPlayer?.seekTo((mediaPlayer?.currentPosition ?: 0) + 15000) },
            onPreviousChapter = {
                val previousChapter = chapters[selectedChapterIndex - 1]
                val encodedPath = Uri.encode(previousChapter.path)
                navController.navigate("media_player/$encodedPath?autoPlay=$isPlaying&resume=false")
            },
            onNextChapter = {
                val nextChapter = chapters[selectedChapterIndex + 1]
                val encodedPath = Uri.encode(nextChapter.path)
                navController.navigate("media_player/$encodedPath?autoPlay=$isPlaying&resume=false")
            },
            isPreviousEnabled = selectedChapterIndex > 0,
            isNextEnabled = selectedChapterIndex < chapters.size - 1
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Grouping Playback Speed and Sleep Timer controls in a Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Wrapping PlaybackSpeedControl in a Box with a specific width
            Box(modifier = Modifier.weight(1f)) {
                PlaybackSpeedControl(
                    currentSpeed = playbackSpeed,
                    onSpeedChange = { speed ->
                        playbackSpeed = speed
                        mediaPlayer?.playbackParams = mediaPlayer?.playbackParams?.setSpeed(speed)!!
                    }
                )
            }

            Spacer(modifier = Modifier.width(16.dp))  // Spacer to add space between the buttons

            // Wrapping SleepTimerControl in a Box with a specific width
            Box(modifier = Modifier.weight(1f)) {
                SleepTimerControl(
                    onTimeSelected = { minutes ->
                        sleepTimerJob?.cancel() // Cancel any existing timer
                        sleepTimerJob = CoroutineScope(Dispatchers.Main).launch {
                            delay(minutes * 60 * 1000L)
                            mediaPlayer?.pause()
                            isPlaying = false
                        }
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Book Progress Indicator with a specific Modifier
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            BookProgressIndicator(
                currentChapterIndex = selectedChapterIndex,
                totalChapters = chapters.size
            )
        }
    }
}


