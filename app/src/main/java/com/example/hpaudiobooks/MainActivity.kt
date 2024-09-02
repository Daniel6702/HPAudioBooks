package com.example.hpaudiobooks

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.hpaudiobooks.about.AboutScreen
import com.example.hpaudiobooks.home.HomeScreen
import com.example.hpaudiobooks.media.MediaPlayerScreen
import com.example.hpaudiobooks.utils.loadBooks
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

            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.secondary // Use primary color as background
            ) {
                NavHost(navController = navController, startDestination = "home_screen") {
                    composable("home_screen") {
                        HomeScreen(navController = navController, audioBooks = audioBooks)
                    }
                    composable("about_screen") {
                        AboutScreen(navController = navController)
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
                            val lastChapterPath = sharedPreferences.getString("${audioBook?.bookData?.name}_last_chapter", null)

                            if (audioBook != null && lastChapterPath != null) {
                                val lastChapter = audioBook.chapters.find { it.path == lastChapterPath }
                                if (lastChapter != null) {
                                    val lastChapterIndex = audioBook.chapters.indexOf(lastChapter)
                                    val resumePosition = sharedPreferences.getInt("${audioBook.bookData.name}_last_position", 0)

                                    MediaPlayerScreen(
                                        bookName = audioBook.bookData.name,
                                        chapterTitle = lastChapter.title,
                                        audioFilePath = lastChapter.path,
                                        onBack = { navController.popBackStack() },
                                        chapters = audioBook.chapters,
                                        currentChapterIndex = lastChapterIndex,
                                        navController = navController,
                                        autoPlay = autoPlay,
                                        resumePosition = resumePosition,
                                        coverImageId = audioBook.coverImageId
                                    )
                                } else {
                                    loadFirstChapter(audioBook, navController, autoPlay)
                                }
                            } else {
                                loadFirstChapter(audioBook, navController, autoPlay)
                            }
                        } else {
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
                                    resumePosition = 0,
                                    coverImageId = audioBook.coverImageId
                                )
                            }
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
                resumePosition = 0,
                coverImageId = audioBook.coverImageId
            )
        }
    }
}



