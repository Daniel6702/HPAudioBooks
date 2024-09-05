package com.example.hpaudiobooks.utils
import android.content.Context
import com.example.hpaudiobooks.models.AudioBook

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