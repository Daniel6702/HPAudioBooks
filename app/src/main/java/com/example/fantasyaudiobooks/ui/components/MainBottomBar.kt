package com.example.fantasyaudiobooks.ui.components

//noinspection UsingMaterialAndMaterial3Libraries
import android.content.Intent
import androidx.compose.material.BottomNavigation
//noinspection UsingMaterialAndMaterial3Libraries
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
import com.example.fantasyaudiobooks.FavoriteActivity
import com.example.fantasyaudiobooks.MainActivity
import com.example.fantasyaudiobooks.SettingsActivity

@Composable
fun MainBottomBar(containerColor: Color = MaterialTheme.colorScheme.primary) {
    val context = LocalContext.current // Get the current context for activity navigation

    BottomNavigation(
        backgroundColor = containerColor,
        contentColor = Color.White
    ) {
        BottomNavigationItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            selected = false, // Manage selection state as needed
            onClick = {
                // Navigate to MainActivity (HomeScreen)
                val intent = Intent(context, MainActivity::class.java)
                context.startActivity(intent)
            },
            selectedContentColor = Color.White,
            unselectedContentColor = Color.Gray
        )
        BottomNavigationItem(
            icon = { Icon(Icons.Default.Favorite, contentDescription = "Favorites") },
            selected = false,
            onClick = {
                // Navigate to FavoriteActivity
                val intent = Intent(context, FavoriteActivity::class.java)
                context.startActivity(intent)
            },
            selectedContentColor = Color.White,
            unselectedContentColor = Color.Gray
        )
        BottomNavigationItem(
            icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
            selected = false,
            onClick = {
                // Navigate to SettingsActivity
                val intent = Intent(context, SettingsActivity::class.java)
                context.startActivity(intent)
            },
            selectedContentColor = Color.White,
            unselectedContentColor = Color.Gray
        )
    }
}