package com.example.hpaudiobooks.components
import android.content.Context
import android.util.Log
import com.example.hpaudiobooks.R
import com.example.hpaudiobooks.models.AudioBook
import com.example.hpaudiobooks.models.BookData
import com.example.hpaudiobooks.models.Chapter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException
import java.io.InputStreamReader

fun loadBooksFromJson(context: Context): List<BookData> {
    val rawResource = context.resources.openRawResource(R.raw.book_data)
    val reader = InputStreamReader(rawResource)
    val bookType = object : TypeToken<List<BookData>>() {}.type
    return Gson().fromJson(reader, bookType)
}

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

fun loadBooks(context: Context): List<AudioBook> {
    val books = loadBooksFromJson(context)
    val audioBooks = mutableListOf<AudioBook>()

    for (book in books) {
        val coverImageId = getCoverImageId(context, book.coverImageName)

        val chapters = loadChaptersForBook(context, book)

        val audioBook = AudioBook(
            bookData = book,
            coverImageId = coverImageId,
            chapters = chapters
        )

        audioBooks.add(audioBook)
    }

    return audioBooks
}

fun getCoverImageId(context: Context, imageName: String): Int {
    return context.resources.getIdentifier(imageName, "drawable", context.packageName)
}