package com.example.fantasyaudiobooks.data.repository

import android.content.Context
import android.util.Log
import com.example.fantasyaudiobooks.R
import com.example.fantasyaudiobooks.data.model.BookSeries
import com.example.fantasyaudiobooks.data.model.BookSeriesWrapper
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStream

class BookRepository(private val context: Context) {

    // Function to load and parse the book series data
    fun getBookSeries(): List<BookSeries> {
        // Load the JSON string from raw resource
        val jsonString = readJsonFileFromRaw(context, R.raw.book_series)

        // Parse the JSON string into a list of BookSeries
        return parseBookSeriesJson(jsonString) ?: emptyList()
    }

    // Helper function to read JSON from the raw resource
    private fun readJsonFileFromRaw(context: Context, resourceId: Int): String? {
        return try {
            val inputStream: InputStream = context.resources.openRawResource(resourceId)
            inputStream.bufferedReader().use { it.readText() }
        } catch (e: Exception) {
            Log.e("JSONReadError", "Error reading JSON file: ${e.message}")
            e.printStackTrace()
            null
        }
    }

    // Helper function to parse the JSON into a list of BookSeries
    private fun parseBookSeriesJson(jsonString: String?): List<BookSeries>? {
        return try {
            if (jsonString == null) return null
            val gson = Gson()
            val wrapperType = object : TypeToken<BookSeriesWrapper>() {}.type
            val bookSeriesWrapper: BookSeriesWrapper = gson.fromJson(jsonString, wrapperType)
            bookSeriesWrapper.book_series
        } catch (e: Exception) {
            Log.e("JSONParseError", "Error parsing JSON: ${e.message}")
            e.printStackTrace()
            null
        }
    }
}
