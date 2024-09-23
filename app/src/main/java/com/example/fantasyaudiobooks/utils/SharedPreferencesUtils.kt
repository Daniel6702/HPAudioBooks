package com.example.fantasyaudiobooks.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.fantasyaudiobooks.data.model.Book
import com.example.fantasyaudiobooks.data.repository.BookRepository

class SharedPreferencesUtils(private val context: Context) {

    private fun getPreferences(name: String) =
        context.getSharedPreferences(name, Context.MODE_PRIVATE)

    private inline fun SharedPreferences.edit(action: SharedPreferences.Editor.() -> Unit) {
        val editor = edit()
        editor.action()
        editor.apply()
    }

    fun saveProgress(bookTitle: String, chapterIndex: Int, position: Int) {
        if (position == 0) return
        getPreferences("book_progress_prefs").edit {
            putInt("${bookTitle}_chapter_index", chapterIndex)
            putInt("${bookTitle}_position", position)
        }
    }

    fun loadProgress(bookTitle: String): Pair<Int, Int> {
        val prefs = getPreferences("book_progress_prefs")
        val chapterIndex = prefs.getInt("${bookTitle}_chapter_index", 0)
        val position = prefs.getInt("${bookTitle}_position", 0)
        return chapterIndex to position
    }

    fun clearAppData() {
        listOf("favorites_prefs", "recent_books_prefs", "book_progress_prefs").forEach {
            getPreferences(it).edit { clear() }
        }
    }

    fun getRecentBooks(): List<String> {
        return getPreferences("recent_books_prefs")
            .getStringSet("recent_books", emptySet())
            ?.toList()
            ?: emptyList()
    }

    fun saveToRecentBooks(bookTitle: String) {
        val prefs = getPreferences("recent_books_prefs")
        prefs.edit {
            val recentBooks = prefs.getStringSet("recent_books", LinkedHashSet<String>())?.toMutableList() ?: mutableListOf()

            recentBooks.remove(bookTitle)
            recentBooks.add(0, bookTitle)

            if (recentBooks.size > 10) {
                putStringSet("recent_books", recentBooks.take(10).toSet())
            } else {
                putStringSet("recent_books", recentBooks.toSet())
            }
        }
    }

    fun getFavoriteBooks(): List<Book> {
        val prefs = getPreferences("favorites_prefs")
        val favoriteBookTitles = prefs.getStringSet("favorites", emptySet()) ?: emptySet()
        val bookRepository = BookRepository(context)
        return bookRepository.getBookSeries()
            .flatMap { it.books }
            .filter { favoriteBookTitles.contains(it.name) }
    }

    fun isBookFavorite(bookTitle: String): Boolean {
        return getPreferences("favorites_prefs")
            .getStringSet("favorites", emptySet())
            ?.contains(bookTitle) == true
    }

    fun toggleFavorite(bookId: String) {
        val prefs = getPreferences("favorites_prefs")
        prefs.edit {
            val favorites = prefs.getStringSet("favorites", emptySet())?.toMutableSet() ?: mutableSetOf()
            if (favorites.contains(bookId)) {
                favorites.remove(bookId)
            } else {
                favorites.add(bookId)
            }
            putStringSet("favorites", favorites)
        }
    }
}

