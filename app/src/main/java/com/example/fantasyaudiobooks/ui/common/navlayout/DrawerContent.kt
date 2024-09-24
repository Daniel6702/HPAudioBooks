package com.example.fantasyaudiobooks.ui.common.navlayout

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.fantasyaudiobooks.data.model.BookSeries

@OptIn(ExperimentalCoilApi::class)
@Composable
fun DrawerContent(
    bookSeriesList: List<BookSeries>,
    onSeriesClick: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface) // Opaque background
    ) {
        // Drawer Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp)
        ) {
            Text(
                text = "Fantasy Audiobooks",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(horizontal = 16.dp),
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        // Divider at the top
        Divider(
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // LazyColumn for the list of series
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 8.dp)
        ) {
            itemsIndexed(bookSeriesList) { index, series ->
                TextButton(
                    onClick = {
                        onSeriesClick(series.id)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Series icon from assets/series_icons
                        Image(
                            painter = rememberImagePainter(data = "file:///android_asset/series_icons/${series.title}.png"),
                            contentDescription = "Series Icon",
                            modifier = Modifier
                                .size(60.dp)
                                .padding(end = 8.dp)
                        )

                        // Series title
                        Text(
                            text = series.title,
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.weight(1f),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                // Divider between series items
                Divider(
                    modifier = Modifier.padding(vertical = 4.dp),
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f)
                )
            }
        }

        // Divider below the list
        Divider(
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
        )
    }
}