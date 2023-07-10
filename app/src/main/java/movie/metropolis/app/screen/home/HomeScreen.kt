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
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import movie.metropolis.app.R
import movie.metropolis.app.feature.play.PlayUpdate
import movie.metropolis.app.screen.Route
import movie.metropolis.app.screen.booking.BookingScreen
import movie.metropolis.app.screen.booking.BookingViewModel
import movie.metropolis.app.screen.cinema.CinemasScreen
import movie.metropolis.app.screen.cinema.CinemasViewModel
import movie.metropolis.app.screen.currentDestinationAsState
import movie.metropolis.app.screen.home.component.HomeScreenContent
import movie.metropolis.app.screen.home.component.HomeScreenToolbar
import movie.metropolis.app.screen.home.component.ProfileIcon
import movie.metropolis.app.screen.listing.ListingScreen
import movie.metropolis.app.screen.listing.ListingViewModel
import movie.metropolis.app.screen.settings.SettingsScreen
import movie.metropolis.app.screen.settings.SettingsViewModel
import movie.style.AppButton
import movie.style.AppNavigationBar
import movie.style.AppNavigationBarItem
import movie.style.AppScaffold
import movie.style.haptic.ClickOnChange
import movie.style.theme.Theme

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    startWith: String,
    controller: NavHostController = rememberNavController(),
    viewModel: HomeViewModel = hiltViewModel(),
    onClickMovie: (String, Boolean) -> Unit,
    onClickCinema: (String) -> Unit,
    onClickUser: () -> Unit,
    onClickLogin: () -> Unit
) {
    val email = viewModel.email
    val destination by controller.currentDestinationAsState()
    val behavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    LaunchedEffect(destination) {
        behavior.state.contentOffset = 0f
    }
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
                Route.Settings() -> stringResource(id = R.string.settings)
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
    ) {
        val moviesState = rememberLazyListState()
        val cinemasState = rememberLazyListState()
        val listing: ListingViewModel = hiltViewModel()
        val cinemas: CinemasViewModel = hiltViewModel()
        val booking: BookingViewModel = hiltViewModel()
        val settings: SettingsViewModel = hiltViewModel()
        HomeScreenContent(
            startWith = Route.by(startWith),
            controller = controller,
            listing = {
                ListingScreen(
                    padding = PaddingValues(),
                    behavior = behavior,
                    onClickMovie = onClickMovie,
                    state = moviesState,
                    viewModel = listing
                )
            },
            cinemas = {
                CinemasScreen(
                    padding = PaddingValues(),
                    onClickCinema = onClickCinema,
                    state = cinemasState,
                    behavior = behavior,
                    viewModel = cinemas
                )
            },
            booking = {
                BookingScreen(
                    padding = PaddingValues(),
                    behavior = behavior,
                    onMovieClick = { onClickMovie(it, true) },
                    viewModel = booking
                )
            },
            settings = {
                SettingsScreen(
                    padding = PaddingValues(),
                    behavior = behavior,
                    viewModel = settings
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
    content: @Composable () -> Unit
) {
    ClickOnChange(route)
    AppScaffold(
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
        navigationBar = {
            AppNavigationBar {
                AppNavigationBarItem(
                    selected = route,
                    index = Route.Movies.destination(),
                    label = { Text(stringResource(R.string.movies)) },
                    icon = { Icon(painterResource(R.drawable.ic_movie), null) },
                    onSelected = onRouteChanged
                )
                AppNavigationBarItem(
                    selected = route,
                    index = Route.Cinemas.destination(),
                    label = { Text(stringResource(R.string.cinemas)) },
                    icon = { Icon(painterResource(R.drawable.ic_cinema), null) },
                    onSelected = onRouteChanged
                )
                AppNavigationBarItem(
                    selected = route,
                    index = Route.Tickets.destination(),
                    label = { Text(stringResource(R.string.tickets)) },
                    icon = { Icon(painterResource(R.drawable.ic_ticket), null) },
                    onSelected = onRouteChanged
                )
                AppNavigationBarItem(
                    selected = route,
                    index = Route.Settings.destination(),
                    label = { Text(stringResource(R.string.settings)) },
                    icon = { Icon(painterResource(R.drawable.ic_settings), null) },
                    onSelected = onRouteChanged
                )
            }
        }
    ) {
        Box(Modifier.padding(it)) {
            content()
            PlayUpdate(modifier = Modifier.align(Alignment.BottomCenter))
        }
    }
}