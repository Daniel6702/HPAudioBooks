package com.example.hpaudiobooks.utils

import android.content.Context
import com.example.hpaudiobooks.R
import com.example.hpaudiobooks.models.BookData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStreamReader

fun loadBooksFromJson(context: Context): List<BookData> {
    val rawResource = context.resources.openRawResource(R.raw.book_data)
    val reader = InputStreamReader(rawResource)
    val bookType = object : TypeToken<List<BookData>>() {}.type
    return Gson().fromJson(reader, bookType)
}