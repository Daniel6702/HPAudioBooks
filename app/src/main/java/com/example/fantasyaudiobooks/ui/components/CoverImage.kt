package com.example.fantasyaudiobooks.ui.components

import android.content.Context
import android.graphics.BitmapFactory
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import java.io.IOException

@Composable
fun loadImageFromAssets(context: Context, imageName: String): Painter? {
    return try {
        val assetManager = context.assets
        val inputStream = assetManager.open("cover_images/$imageName")
        BitmapFactory.decodeStream(inputStream)?.let {
            BitmapPainter(it.asImageBitmap())
        }
    } catch (e: IOException) {
        e.printStackTrace()
        null
    }
}