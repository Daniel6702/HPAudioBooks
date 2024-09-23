package com.example.fantasyaudiobooks.utils

import android.content.Context
import android.util.Log
import java.io.InputStream

// Utility function to read a JSON file from the res/raw directory
fun readJsonFileFromRaw(context: Context, resourceId: Int): String? {
    return try {
        val inputStream: InputStream = context.resources.openRawResource(resourceId)
        inputStream.bufferedReader().use { it.readText() }
    } catch (e: Exception) {
        Log.e("JSONReadError", "Error reading JSON file: ${e.message}")
        e.printStackTrace()
        null
    }
}