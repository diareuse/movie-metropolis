package movie.metropolis.app.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import movie.metropolis.app.R
import movie.metropolis.app.screen.booking.BookingScreen
import movie.metropolis.app.screen.booking.BookingViewModel
import movie.metropolis.app.screen.cinema.CinemasScreen
import movie.metropolis.app.screen.cinema.CinemasViewModel
import movie.metropolis.app.screen.listing.ListingViewModel
import movie.metropolis.app.screen.listing.MoviesScreen

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HomeScreen(
    listing: ListingViewModel = hiltViewModel(),
    cinemas: CinemasViewModel = hiltViewModel(),
    booking: BookingViewModel = hiltViewModel(),
    moviesState: LazyListState = rememberLazyListState(),
    moviesAvailableState: PagerState = rememberPagerState(),
    moviesUpcomingState: LazyListState = rememberLazyListState(),
    cinemasState: LazyListState = rememberLazyListState(),
    bookingState: LazyListState = rememberLazyListState(),
    onPermissionsRequested: suspend (Array<String>) -> Boolean,
    onClickMovie: (String, Boolean) -> Unit,
    onClickCinema: (String) -> Unit
) {
    HomeScreen(
        movies = {
            MoviesScreen(
                padding = it,
                onClickMovie = onClickMovie,
                state = moviesState,
                stateAvailable = moviesAvailableState,
                stateUpcoming = moviesUpcomingState,
                viewModel = listing,
                onPermissionsRequested = onPermissionsRequested
            )
        },
        cinemas = {
            CinemasScreen(
                padding = it,
                onPermissionRequested = onPermissionsRequested,
                onClickCinema = onClickCinema,
                viewModel = cinemas,
                state = cinemasState
            )
        },
        booking = {
            BookingScreen(
                padding = it,
                viewModel = booking,
                state = bookingState
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreen(
    movies: @Composable (PaddingValues) -> Unit,
    cinemas: @Composable (PaddingValues) -> Unit,
    booking: @Composable (PaddingValues) -> Unit,
) {
    val (selected, onChanged) = rememberSaveable { mutableStateOf(0) }
    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color.Transparent,
                modifier = Modifier
                    .shadow(32.dp)
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                SelectableNavigationBarItem(
                    selected = selected,
                    index = 0,
                    icon = R.drawable.ic_movie,
                    label = "Movies",
                    onSelected = onChanged
                )
                SelectableNavigationBarItem(
                    selected = selected,
                    index = 1,
                    icon = R.drawable.ic_cinema,
                    label = "Cinemas",
                    onSelected = onChanged
                )
                SelectableNavigationBarItem(
                    selected = selected,
                    index = 2,
                    icon = R.drawable.ic_ticket,
                    label = "Tickets",
                    onSelected = onChanged
                )
            }
        }
    ) {
        when (selected) {
            0 -> movies(it)
            1 -> cinemas(it)
            2 -> booking(it)
        }
    }
}

@Composable
fun RowScope.SelectableNavigationBarItem(
    selected: Int,
    index: Int,
    icon: Int,
    label: String,
    onSelected: (Int) -> Unit
) {
    NavigationBarItem(
        selected = selected == index,
        onClick = { onSelected(index) },
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = MaterialTheme.colorScheme.primary,
            indicatorColor = MaterialTheme.colorScheme.surface.copy(alpha = 0f),
            unselectedIconColor = MaterialTheme.colorScheme.surfaceVariant,
            unselectedTextColor = MaterialTheme.colorScheme.surfaceVariant,
            selectedTextColor = MaterialTheme.colorScheme.primary,
        ),
        icon = {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = null
            )
        },
        label = { Text(label) }
    )
}