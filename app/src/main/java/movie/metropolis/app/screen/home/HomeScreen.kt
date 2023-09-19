package movie.metropolis.app.screen.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.res.*
import androidx.compose.ui.semantics.*
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
import movie.metropolis.app.screen.home.component.ProfileIcon
import movie.metropolis.app.screen.home.component.rememberInstantApp
import movie.metropolis.app.screen.home.component.rememberScreenState
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

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalComposeUiApi::class
)
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
    val instantApp = rememberInstantApp()
    HomeScreen(
        isLoggedIn = email != null,
        isInstantApp = instantApp.isInstant,
        route = destination?.route ?: startWith,
        onRouteChanged = listener@{
            if (destination?.route == it) return@listener
            while (controller.popBackStack()) {
                /* no-op */
            }
            controller.navigate(it)
        },
        onNavigateToLogin = onClickLogin,
        onNavigateToInstall = instantApp::install
    ) {
        val listing = rememberScreenState<ListingViewModel>()
        val cinemas = rememberScreenState<CinemasViewModel>()
        val booking = rememberScreenState<BookingViewModel>()
        val settings = rememberScreenState<SettingsViewModel>()
        val profileIcon = @Composable {
            if (email != null) ProfileIcon(
                email = email,
                onClick = onClickUser
            )
        }
        HomeScreenContent(
            modifier = Modifier.semantics {
                testTagsAsResourceId = true
            },
            startWith = Route.by(startWith),
            controller = controller,
            listing = {
                ListingScreen(
                    behavior = listing.behavior,
                    state = listing.list,
                    viewModel = listing.viewModel,
                    profileIcon = profileIcon,
                    onClickMovie = onClickMovie
                )
            },
            cinemas = {
                CinemasScreen(
                    behavior = cinemas.behavior,
                    state = cinemas.list,
                    viewModel = cinemas.viewModel,
                    profileIcon = profileIcon,
                    onClickCinema = onClickCinema
                )
            },
            booking = {
                BookingScreen(
                    behavior = booking.behavior,
                    viewModel = booking.viewModel,
                    profileIcon = profileIcon,
                    onMovieClick = { onClickMovie(it, true) }
                )
            },
            settings = {
                SettingsScreen(
                    behavior = settings.behavior,
                    viewModel = settings.viewModel,
                    profileIcon = profileIcon,
                )
            }
        )
    }
}

@Composable
private fun HomeScreen(
    isLoggedIn: Boolean,
    isInstantApp: Boolean,
    route: String,
    onNavigateToLogin: () -> Unit,
    onNavigateToInstall: () -> Unit,
    onRouteChanged: (String) -> Unit,
    content: @Composable () -> Unit
) {
    ClickOnChange(route)
    AppScaffold(
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets
            .only(WindowInsetsSides.Bottom + WindowInsetsSides.Horizontal),
        floatingActionButton = {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                if (!isLoggedIn) AppButton(
                    onClick = onNavigateToLogin,
                    containerColor = Theme.color.container.error,
                    contentColor = Theme.color.content.error,
                    elevation = 16.dp
                ) {
                    Text(stringResource(R.string.sign_in))
                }
                if (isInstantApp) AppButton(
                    onClick = onNavigateToInstall,
                    elevation = 16.dp
                ) {
                    Text(stringResource(id = R.string.install))
                }
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