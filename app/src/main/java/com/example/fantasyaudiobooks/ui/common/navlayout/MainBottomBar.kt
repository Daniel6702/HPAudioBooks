package com.example.fantasyaudiobooks.ui.common.navlayout

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.example.fantasyaudiobooks.ui.favorites.FavoriteActivity
import com.example.fantasyaudiobooks.ui.home.MainActivity
import com.example.fantasyaudiobooks.ui.mediaplayer.MediaPlayerActivity
import com.example.fantasyaudiobooks.ui.settings.SettingsActivity

@Composable
fun MainBottomBar(containerColor: Color = MaterialTheme.colorScheme.primary) {
    val context = LocalContext.current
    val activity = context as? Activity

    BottomNavigation(
        backgroundColor = containerColor,
        contentColor = Color.White
    ) {
        BottomNavigationItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            selected = false,
            onClick = { navigateIfNeeded(context, activity, MainActivity::class.java) },
            selectedContentColor = Color.White,
            unselectedContentColor = Color.Gray
        )
        BottomNavigationItem(
            icon = { Icon(Icons.Default.Favorite, contentDescription = "Favorites") },
            selected = false,
            onClick = { navigateIfNeeded(context, activity, FavoriteActivity::class.java) },
            selectedContentColor = Color.White,
            unselectedContentColor = Color.Gray
        )
        BottomNavigationItem(
            icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
            selected = false,
            onClick = { navigateIfNeeded(context, activity, SettingsActivity::class.java) },
            selectedContentColor = Color.White,
            unselectedContentColor = Color.Gray
        )
    }
}

fun navigateIfNeeded(
    context: Context,
    currentActivity: Activity?,
    targetActivityClass: Class<out Activity>
) {
    if (currentActivity == null || currentActivity::class.java != targetActivityClass) {     // Check if the current activity is not the target activity
        val intent = Intent(context, targetActivityClass)
        context.startActivity(intent)
        if (currentActivity is MediaPlayerActivity) {         // Only finish if the current activity is MediaPlayerActivity
            currentActivity.finish()
        }
    }
}