package com.example.fantasyaudiobooks.utils

import android.util.Log
import com.example.fantasyaudiobooks.data.model.BookSeries
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

fun parseBookSeriesJson(jsonString: String?): List<BookSeries>? {
    return try {
        if (jsonString == null) return null
        val gson = Gson()
        val listType = object : TypeToken<List<BookSeries>>() {}.type
        gson.fromJson<List<BookSeries>>(jsonString, listType)
    } catch (e: Exception) {
        Log.e("JSONParseError", "Error parsing JSON: ${e.message}")
        e.printStackTrace()
        null
    }
}