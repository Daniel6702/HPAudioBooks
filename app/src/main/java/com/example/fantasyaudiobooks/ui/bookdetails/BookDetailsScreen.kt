package com.example.fantasyaudiobooks.ui.bookdetails

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.fantasyaudiobooks.data.model.Book
import com.example.fantasyaudiobooks.data.model.BookSeries
import com.example.fantasyaudiobooks.ui.common.navlayout.BaseLayout

@Composable
fun BookDetailsScreen(
    book: Book,
    bookSeriesList: List<BookSeries>,
    paddingValues: PaddingValues,
    onSeriesClick: (Int) -> Unit,
    onListenClick: () -> Unit
) {
    val backPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    BaseLayout(
        bookSeriesList = bookSeriesList,
        onSeriesClick = onSeriesClick,
        containerColor = MaterialTheme.colorScheme.primary
    ) {
        Box(modifier = Modifier.padding(paddingValues)) {
            BookFrontPage(
                audioBook = AudioBook(book),
                onBackClick = { backPressedDispatcher?.onBackPressed() },
                onListenClick = onListenClick
            )
        }
    }
}