package com.example.fantasyaudiobooks.ui.baselayout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.example.fantasyaudiobooks.data.model.BookSeries
import kotlinx.coroutines.launch

@Composable
fun BaseLayout(
    bookSeriesList: List<BookSeries>,
    onSeriesClick: (Int) -> Unit,
    containerColor: Color = MaterialTheme.colorScheme.primary,
    content: @Composable (PaddingValues) -> Unit
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    // Search state
    var isSearching by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            Box(
                modifier = Modifier
                    .width(LocalConfiguration.current.screenWidthDp.dp * 0.8f)
                    .background(MaterialTheme.colorScheme.background)
            ) {
                DrawerContent(
                    bookSeriesList = bookSeriesList,
                    onSeriesClick = { seriesId ->
                        coroutineScope.launch { drawerState.close() }
                        onSeriesClick(seriesId)
                    }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                MainTopBar(
                    onMenuClick = { coroutineScope.launch { drawerState.open() } },
                    onSearchQueryChanged = { query ->
                        searchQuery = query
                    },
                    isSearching = isSearching,
                    onSearchToggle = {
                        isSearching = !isSearching
                        if (!isSearching) {
                            searchQuery = ""
                        }
                    },
                    searchQuery = searchQuery,
                    containerColor = containerColor
                )
            },
            bottomBar = {
                MainBottomBar(containerColor = containerColor)
            },
            content = { paddingValues ->
                if (isSearching && searchQuery.isNotBlank()) {
                    // Display search results
                    SearchResultsScreen(
                        searchQuery = searchQuery,
                        bookSeriesList = bookSeriesList,
                        paddingValues = paddingValues
                    )
                } else {
                    // Display the normal content
                    content(paddingValues)
                }
            }
        )
    }
}