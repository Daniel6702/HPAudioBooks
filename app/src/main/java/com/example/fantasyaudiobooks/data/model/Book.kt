package com.example.fantasyaudiobooks.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Book(
    val id: Int,
    val coverImageName: String,
    val name: String,
    val author: String,
    val releaseYear: String,
    val lengthInMinutes: Int,
    val narrator: String,
    val description: String,
    val primary: String,
    val secondary: String,
    val tertiary: String,
    val quaternary: String,
    val chapters: List<Chapter>
) : Parcelable