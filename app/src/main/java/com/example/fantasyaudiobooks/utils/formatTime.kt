package com.example.fantasyaudiobooks.utils

import android.annotation.SuppressLint

@SuppressLint("DefaultLocale")
fun formatTime(milliseconds: Int): String {
    val totalSeconds = milliseconds / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format("%02d:%02d", minutes, seconds)
}

fun formatLength(lengthInMinutes: Int): String {
    val hours = lengthInMinutes / 60
    val minutes = lengthInMinutes % 60
    return if (hours > 0) {
        "$hours hr ${minutes} min"
    } else {
        "$minutes min"
    }
}