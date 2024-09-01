package com.example.hpaudiobooks

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.hpaudiobooks.components.BookList
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.hpaudiobooks.components.MediaPlayerScreen
import com.example.hpaudiobooks.components.loadBooks
import com.example.hpaudiobooks.models.AudioBook

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val audioBooks = remember { mutableStateListOf<AudioBook>() }

            LaunchedEffect(Unit) {
                val loadedBooks = loadBooks(this@MainActivity)
                audioBooks.addAll(loadedBooks)
            }

            NavHost(navController = navController, startDestination = "book_list") {
                composable("book_list") {
                    BookList(books = audioBooks, navController = navController)
                }
                composable(
                    "media_player/{chapterPath}?autoPlay={autoPlay}&resume={resume}",
                    arguments = listOf(
                        navArgument("autoPlay") { defaultValue = "false" },
                        navArgument("resume") { defaultValue = "false" }
                    )
                ) { backStackEntry ->
                    val encodedChapterPath = backStackEntry.arguments?.getString("chapterPath") ?: ""
                    val chapterPath = Uri.decode(encodedChapterPath)
                    val autoPlay = backStackEntry.arguments?.getString("autoPlay")?.toBoolean() ?: false
                    val resume = backStackEntry.arguments?.getString("resume")?.toBoolean() ?: false

                    val sharedPreferences = LocalContext.current.getSharedPreferences("audiobook_prefs", Context.MODE_PRIVATE)

                    val audioBook = audioBooks.find { it.chapters.any { chapter -> chapter.path == chapterPath } }

                    if (resume) {
                        Log.d("MainActivity", "Trying to resume playback")

                        // Retrieve the last saved chapter and position
                        val lastChapterPath = sharedPreferences.getString("${audioBook?.bookData?.name}_last_chapter", null)

                        if (audioBook != null && lastChapterPath != null) {
                            val lastChapter = audioBook.chapters.find { it.path == lastChapterPath }
                            if (lastChapter != null) {
                                val lastChapterIndex = audioBook.chapters.indexOf(lastChapter)
                                val resumePosition = sharedPreferences.getInt("${audioBook.bookData.name}_last_position", 0)

                                Log.d("MainActivity", "Resuming from chapter: ${lastChapter.path} at position: $resumePosition")

                                MediaPlayerScreen(
                                    bookName = audioBook.bookData.name,
                                    chapterTitle = lastChapter.title,
                                    audioFilePath = lastChapter.path,
                                    onBack = { navController.popBackStack() },
                                    chapters = audioBook.chapters,
                                    currentChapterIndex = lastChapterIndex,
                                    navController = navController,
                                    autoPlay = autoPlay,
                                    resumePosition = resumePosition,  // Resume from the last position,
                                    coverImageId = audioBook.coverImageId
                                )
                            } else {
                                Log.e("BookLoading", "Could not find chapter for path: $lastChapterPath")
                                // Fallback to first chapter if the last chapter is not found
                                loadFirstChapter(audioBook, navController, autoPlay)
                            }
                        } else {
                            Log.d("MainActivity", "No saved state found, starting from the first chapter")
                            // No saved state, start from the first chapter
                            loadFirstChapter(audioBook, navController, autoPlay)
                        }
                    } else {
                        Log.d("MainActivity", "Regular navigation without resuming")
                        val currentChapter = audioBook?.chapters?.find { it.path == chapterPath }

                        if (audioBook != null && currentChapter != null) {
                            val currentChapterIndex = audioBook.chapters.indexOf(currentChapter)
                            MediaPlayerScreen(
                                bookName = audioBook.bookData.name,
                                chapterTitle = currentChapter.title,
                                audioFilePath = currentChapter.path,
                                onBack = { navController.popBackStack() },
                                chapters = audioBook.chapters,
                                currentChapterIndex = currentChapterIndex,
                                navController = navController,
                                autoPlay = autoPlay,
                                resumePosition = 0,  // Start from the beginning of the chapter
                                coverImageId = audioBook.coverImageId
                            )
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun loadFirstChapter(audioBook: AudioBook?, navController: NavController, autoPlay: Boolean) {
        if (audioBook != null) {
            val firstChapter = audioBook.chapters.first()
            val firstChapterIndex = 0
            MediaPlayerScreen(
                bookName = audioBook.bookData.name,
                chapterTitle = firstChapter.title,
                audioFilePath = firstChapter.path,
                onBack = { navController.popBackStack() },
                chapters = audioBook.chapters,
                currentChapterIndex = firstChapterIndex,
                navController = navController,
                autoPlay = autoPlay,
                resumePosition = 0,  // Start from the beginning of the first chapter
                coverImageId = audioBook.coverImageId
            )
        }
    }
}


