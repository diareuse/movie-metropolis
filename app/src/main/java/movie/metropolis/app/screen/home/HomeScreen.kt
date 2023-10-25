package movie.metropolis.app.screen.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.res.*
import androidx.compose.ui.semantics.*
import androidx.compose.ui.unit.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import movie.metropolis.app.R
import movie.metropolis.app.feature.play.PlayUpdate
import movie.metropolis.app.screen.Route
import movie.metropolis.app.screen.currentDestinationAsState
import movie.metropolis.app.screen.home.component.HomeScreenContent
import movie.metropolis.app.screen.home.component.rememberInstantApp
import movie.style.AppButton
import movie.style.AppNavigationBar
import movie.style.AppNavigationBarItem
import movie.style.AppScaffold
import movie.style.theme.Theme

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun HomeScreen(
    loggedIn: Boolean,
    listing: @Composable (PaddingValues) -> Unit,
    cinemas: @Composable (PaddingValues) -> Unit,
    booking: @Composable (PaddingValues) -> Unit,
    settings: @Composable (PaddingValues) -> Unit,
    startWith: String,
    onClickLogin: () -> Unit,
    modifier: Modifier = Modifier,
    controller: NavHostController = rememberNavController(),
) {
    val destination by controller.currentDestinationAsState()
    val instantApp = rememberInstantApp()
    HomeScreen(
        modifier = modifier,
        isLoggedIn = loggedIn,
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
    ) { padding ->
        HomeScreenContent(
            modifier = Modifier.semantics {
                testTagsAsResourceId = true
            },
            startWith = Route.by(startWith),
            controller = controller,
            listing = listing,
            cinemas = cinemas,
            booking = booking,
            settings = settings,
            contentPadding = padding
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
    modifier: Modifier = Modifier,
    content: @Composable (PaddingValues) -> Unit
) {
    AppScaffold(
        modifier = modifier,
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
    ) { padding ->
        Box {
            content(padding)
            PlayUpdate(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(padding)
            )
        }
    }
}