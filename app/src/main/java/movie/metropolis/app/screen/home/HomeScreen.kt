package movie.metropolis.app.screen.home

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.res.*
import androidx.compose.ui.unit.*
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import movie.metropolis.app.R
import movie.metropolis.app.feature.play.PlayUpdate
import movie.metropolis.app.screen.Route
import movie.metropolis.app.screen.booking.BookingScreen
import movie.metropolis.app.screen.booking.BookingViewModel
import movie.metropolis.app.screen.cinema.CinemasScreen
import movie.metropolis.app.screen.cinema.CinemasViewModel
import movie.metropolis.app.screen.currentDestinationAsState
import movie.metropolis.app.screen.detail.plus
import movie.metropolis.app.screen.home.component.HomeScreenContent
import movie.metropolis.app.screen.home.component.ProfileIcon
import movie.metropolis.app.screen.home.component.SelectableNavigationBarItem
import movie.metropolis.app.screen.listing.ListingScreen
import movie.metropolis.app.screen.listing.ListingViewModel
import movie.style.AppButton
import movie.style.haptic.ClickOnChange
import movie.style.theme.Theme

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    startWith: String,
    viewModel: HomeViewModel = hiltViewModel(),
    onClickMovie: (String, Boolean) -> Unit,
    onClickCinema: (String) -> Unit,
    onClickUser: () -> Unit,
    onClickLogin: () -> Unit
) {
    val email = viewModel.email
    val controller = rememberAnimatedNavController()
    val destination by controller.currentDestinationAsState()
    HomeScreen(
        isLoggedIn = email != null,
        route = destination?.route ?: startWith,
        onRouteChanged = listener@{
            if (destination?.route == it) return@listener
            while (controller.popBackStack()) {
                /* no-op */
            }
            controller.navigate(it)
        },
        onNavigateToLogin = onClickLogin
    ) { padding ->
        val moviesState = rememberLazyListState()
        val cinemasState = rememberLazyListState()
        val listing: ListingViewModel = hiltViewModel()
        val cinemas: CinemasViewModel = hiltViewModel()
        val booking: BookingViewModel = hiltViewModel()
        HomeScreenContent(
            startWith = Route.by(startWith),
            controller = controller,
            icon = {
                if (email != null) ProfileIcon(
                    email = email,
                    onClick = onClickUser
                )
            },
            listing = { inner, behavior ->
                ListingScreen(
                    padding = padding + inner,
                    behavior = behavior,
                    onClickMovie = onClickMovie,
                    state = moviesState,
                    viewModel = listing
                )
            },
            cinemas = { inner, behavior ->
                CinemasScreen(
                    padding = padding + inner,
                    onClickCinema = onClickCinema,
                    state = cinemasState,
                    behavior = behavior,
                    viewModel = cinemas
                )
            },
            booking = { inner, behavior ->
                BookingScreen(
                    padding = padding + inner,
                    behavior = behavior,
                    onMovieClick = { onClickMovie(it, true) },
                    viewModel = booking
                )
            }
        )
    }
}

@Composable
private fun HomeScreen(
    isLoggedIn: Boolean,
    route: String,
    onNavigateToLogin: () -> Unit,
    onRouteChanged: (String) -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    ClickOnChange(route)
    Scaffold(
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets
            .only(WindowInsetsSides.Bottom + WindowInsetsSides.Horizontal),
        floatingActionButton = {
            if (!isLoggedIn) AppButton(
                onClick = onNavigateToLogin,
                containerColor = Theme.color.container.error,
                contentColor = Theme.color.content.error,
                elevation = 16.dp
            ) {
                Text(stringResource(R.string.sign_in))
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        bottomBar = {
            Column {
                PlayUpdate(modifier = Modifier.align(Alignment.CenterHorizontally))
                Surface(
                    tonalElevation = 1.dp,
                    shadowElevation = 32.dp,
                    color = Theme.color.container.background,
                    contentColor = Theme.color.content.background
                ) {
                    NavigationBar(
                        containerColor = Color.Transparent
                    ) {
                        SelectableNavigationBarItem(
                            selected = route,
                            index = Route.Movies.destination(),
                            icon = R.drawable.ic_movie,
                            label = stringResource(R.string.movies),
                            onSelected = onRouteChanged
                        )
                        SelectableNavigationBarItem(
                            selected = route,
                            index = Route.Cinemas.destination(),
                            icon = R.drawable.ic_cinema,
                            label = stringResource(R.string.cinemas),
                            onSelected = onRouteChanged
                        )
                        SelectableNavigationBarItem(
                            selected = route,
                            index = Route.Tickets.destination(),
                            icon = R.drawable.ic_ticket,
                            label = stringResource(R.string.tickets),
                            onSelected = onRouteChanged
                        )
                    }
                }
            }
        }
    ) {
        content(it)
    }
}