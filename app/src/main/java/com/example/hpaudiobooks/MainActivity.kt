package com.example.hpaudiobooks

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.hpaudiobooks.about.AboutScreen
import com.example.hpaudiobooks.media.MediaPlayerScreen
import com.example.hpaudiobooks.utils.loadBooks
import com.example.hpaudiobooks.models.AudioBook
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val audioBooks = remember { mutableStateListOf<AudioBook>() }

            LaunchedEffect(Unit) {
                val loadedBooks = loadBooks(this@MainActivity)
                audioBooks.addAll(loadedBooks)

                // Automatically navigate to the first book when the app opens
                if (audioBooks.isNotEmpty()) {
                    navController.navigate("book_page/${audioBooks.first().bookData.name}")
                }
            }

            val drawerState = rememberDrawerState(DrawerValue.Closed)
            val scope = rememberCoroutineScope()

            // ModalNavigationDrawer with proper layout and opaque background
            ModalNavigationDrawer(
                drawerState = drawerState,
                scrimColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.32f), // Scrim with slight transparency
                drawerContent = {
                    Surface(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(280.dp)  // 80% of a typical screen width
                            .background(MaterialTheme.colorScheme.surface), // Opaque background for the drawer
                        color = MaterialTheme.colorScheme.surface // Ensure opaque background
                    ) {
                        DrawerContent(
                            books = audioBooks,
                            onBookSelected = { bookIndex ->
                                val bookName = audioBooks[bookIndex].bookData.name
                                navController.navigate("book_page/$bookName")
                                scope.launch { drawerState.close() } // Close drawer after selecting a book
                            },
                            navController = navController,
                            drawerState = drawerState // Pass drawerState to DrawerContent
                        )
                    }
                }
            ) {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("AudioBooks") },
                            navigationIcon = {
                                IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                    Icon(Icons.Default.Menu, contentDescription = "Menu")
                                }
                            }
                        )
                    }
                ) { paddingValues ->
                    NavHost(
                        navController = navController,
                        startDestination = "book_page/{bookName}",
                        modifier = Modifier.padding(paddingValues)
                    ) {
                        composable("book_page/{bookName}") { backStackEntry ->
                            val bookName = backStackEntry.arguments?.getString("bookName") ?: ""
                            val audioBook = audioBooks.find { it.bookData.name == bookName }

                            if (audioBook != null) {
                                BookFrontPage(
                                    audioBook = audioBook,
                                    onListenClick = {
                                        val firstChapter = audioBook.chapters.first()
                                        val encodedPath = Uri.encode(firstChapter.path)
                                        navController.navigate("media_player/$encodedPath?autoPlay=true&resume=false")
                                    }
                                )
                            }
                        }

                        composable("about_screen") {
                            AboutScreen(navController = navController)
                        }
                        // Media player screen
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


