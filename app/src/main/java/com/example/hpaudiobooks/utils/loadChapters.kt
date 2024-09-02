package com.example.hpaudiobooks.utils

import android.content.Context
import android.util.Log
import com.example.hpaudiobooks.models.BookData
import com.example.hpaudiobooks.models.Chapter
import java.io.IOException

fun getBookIndex(context: Context, bookName: String): Int {
    val books = loadBooksFromJson(context)
    return books.indexOfFirst { it.name == bookName } + 1
}

fun loadChaptersForBook(context: Context, book: BookData): List<Chapter> {
    val bookPath = "${String.format("%02d", getBookIndex(context, book.name))}_${book.name.replace(" ", "_")}"
    Log.d("BookLoading", "Loading chapters from path: $bookPath")  // Add log here
    val chapters = mutableListOf<Chapter>()

    try {
        // Access the assets directory corresponding to the book's path
        val chapterFiles = context.assets.list(bookPath) ?: arrayOf()
        Log.d("BookLoading", "Found ${chapterFiles.size} chapters for book: ${book.name}")  // Add log here

        // Sort the chapter files if necessary
        chapterFiles.sort()

        // Iterate over the chapter files and create Chapter objects
        chapterFiles.forEachIndexed { index, fileName ->
            val chapter = Chapter(
                index = index + 1,
                title = fileName.removeSuffix(".mp3").replace("_", " "),
                path = "$bookPath/$fileName"
            )
            chapters.add(chapter)
        }
    } catch (e: IOException) {
        Log.e("BookLoading", "Error loading chapters for book: ${book.name}", e)  // Add log here
    }

    return chapters
}