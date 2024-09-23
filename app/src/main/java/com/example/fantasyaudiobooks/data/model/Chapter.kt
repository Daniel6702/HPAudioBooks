package com.example.fantasyaudiobooks.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Chapter(
    val id: Int,
    val name: String,
    val url: String
) : Parcelable