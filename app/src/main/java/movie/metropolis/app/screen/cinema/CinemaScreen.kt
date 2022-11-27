package movie.metropolis.app.screen.cinema

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import movie.metropolis.app.R
import movie.metropolis.app.model.CinemaView
import movie.metropolis.app.model.MovieBookingView
import movie.metropolis.app.screen.Loadable
import movie.metropolis.app.screen.detail.DatePickerRow
import movie.metropolis.app.screen.detail.plus
import movie.metropolis.app.view.textPlaceholder
import java.util.Date

@Composable
fun CinemaScreen(
    viewModel: CinemaViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onBookingClick: (String) -> Unit
) {
    val cinema by viewModel.cinema.collectAsState()
    val items by viewModel.items.collectAsState()
    val selectedDate by viewModel.selectedDate.collectAsState()
    CinemaScreen(
        cinema = cinema,
        items = items,
        selectedDate = selectedDate,
        onSelectedDateChanged = { viewModel.selectedDate.value = it },
        onBookingClick = onBookingClick,
        onBackClick = onBackClick
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
private fun CinemaScreen(
    cinema: Loadable<CinemaView>,
    items: Loadable<List<MovieBookingView>>,
    selectedDate: Date,
    onSelectedDateChanged: (Date) -> Unit,
    onBookingClick: (String) -> Unit,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth(.8f)
                            .textPlaceholder(cinema.isLoading),
                        text = cinema.getOrNull()?.name?.substringBefore(',') ?: "Loading…",
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    IconButton(onBackClick) {
                        Icon(painterResource(id = R.drawable.ic_back), null)
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = padding + PaddingValues(vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                DatePickerRow(
                    start = remember { Date() },
                    selected = selectedDate,
                    onClickDate = onSelectedDateChanged
                )
            }
            items(items.getOrNull().orEmpty(), key = { it.movie.id }) {
                MovieShowingItem(
                    modifier = Modifier
                        .animateItemPlacement()
                        .padding(horizontal = 24.dp),
                    movie = it.movie,
                    availability = it.availability,
                    onClick = onBookingClick
                )
            }
        }
    }
}