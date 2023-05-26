package movie.metropolis.app.screen.home

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
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
import movie.metropolis.app.screen.home.component.BottomNavigationBar
import movie.metropolis.app.screen.home.component.HomeScreenContent
import movie.metropolis.app.screen.home.component.HomeScreenToolbar
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
    val behavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    HomeScreen(
        isLoggedIn = email != null,
        route = destination?.route ?: startWith,
        behavior = behavior,
        onRouteChanged = listener@{
            if (destination?.route == it) return@listener
            while (controller.popBackStack()) {
                /* no-op */
            }
            controller.navigate(it)
        },
        title = {
            val text = when (destination?.route) {
                Route.Cinemas() -> stringResource(id = R.string.cinemas)
                Route.Movies() -> stringResource(id = R.string.movies)
                Route.Tickets() -> stringResource(id = R.string.tickets)
                else -> ""
            }
            Text(text)
        },
        profileIcon = {
            if (email != null) ProfileIcon(
                email = email,
                onClick = onClickUser
            )
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
            listing = {
                ListingScreen(
                    padding = padding,
                    behavior = behavior,
                    onClickMovie = onClickMovie,
                    state = moviesState,
                    viewModel = listing
                )
            },
            cinemas = {
                CinemasScreen(
                    padding = padding,
                    onClickCinema = onClickCinema,
                    state = cinemasState,
                    behavior = behavior,
                    viewModel = cinemas
                )
            },
            booking = {
                BookingScreen(
                    padding = padding,
                    behavior = behavior,
                    onMovieClick = { onClickMovie(it, true) },
                    viewModel = booking
                )
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreen(
    isLoggedIn: Boolean,
    route: String,
    onNavigateToLogin: () -> Unit,
    title: @Composable () -> Unit,
    profileIcon: @Composable () -> Unit,
    onRouteChanged: (String) -> Unit,
    behavior: TopAppBarScrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(),
    content: @Composable (PaddingValues) -> Unit
) {
    ClickOnChange(route)
    Scaffold(
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets
            .only(WindowInsetsSides.Bottom + WindowInsetsSides.Horizontal),
        topBar = {
            HomeScreenToolbar(
                profileIcon = profileIcon,
                behavior = behavior,
                title = title
            )
        },
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
                BottomNavigationBar {
                    SelectableNavigationBarItem(
                        selected = route,
                        index = Route.Movies.destination(),
                        label = { Text(stringResource(R.string.movies)) },
                        icon = { Icon(painterResource(R.drawable.ic_movie), null) },
                        onSelected = onRouteChanged
                    )
                    SelectableNavigationBarItem(
                        selected = route,
                        index = Route.Cinemas.destination(),
                        label = { Text(stringResource(R.string.cinemas)) },
                        icon = { Icon(painterResource(R.drawable.ic_cinema), null) },
                        onSelected = onRouteChanged
                    )
                    SelectableNavigationBarItem(
                        selected = route,
                        index = Route.Tickets.destination(),
                        label = { Text(stringResource(R.string.tickets)) },
                        icon = { Icon(painterResource(R.drawable.ic_ticket), null) },
                        onSelected = onRouteChanged
                    )
                }
            }
        }
    ) {
        content(it)
    }
}