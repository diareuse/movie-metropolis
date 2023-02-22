package movie.metropolis.app.screen.home

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.res.*
import androidx.compose.ui.unit.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import movie.metropolis.app.R
import movie.metropolis.app.feature.play.PlayUpdate
import movie.metropolis.app.screen.Route
import movie.metropolis.app.screen.booking.BookingScreen
import movie.metropolis.app.screen.booking.BookingViewModel
import movie.metropolis.app.screen.cinema.CinemasScreen
import movie.metropolis.app.screen.cinema.CinemasViewModel
import movie.metropolis.app.screen.currentDestinationAsState
import movie.metropolis.app.screen.detail.plus
import movie.metropolis.app.screen.listing.ListingScreen
import movie.metropolis.app.screen.listing.ListingViewModel
import movie.style.AppButton
import movie.style.AppIconButton
import movie.style.AppImage
import movie.style.AppToolbar
import movie.style.haptic.ClickOnChange
import movie.style.theme.Theme
import java.security.MessageDigest

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
        HomeScreenContent(
            startWith = startWith,
            email = email,
            controller = controller,
            padding = padding,
            onClickCinema = onClickCinema,
            onClickMovie = onClickMovie,
            onClickUser = onClickUser
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
private fun HomeScreenContent(
    startWith: String,
    email: String?,
    controller: NavHostController,
    padding: PaddingValues,
    listing: ListingViewModel = hiltViewModel(),
    cinemas: CinemasViewModel = hiltViewModel(),
    booking: BookingViewModel = hiltViewModel(),
    moviesState: LazyListState = rememberLazyListState(),
    cinemasState: LazyListState = rememberLazyListState(),
    bookingState: LazyListState = rememberLazyListState(),
    onClickMovie: (String, Boolean) -> Unit,
    onClickCinema: (String) -> Unit,
    onClickUser: () -> Unit
) {
    val state = rememberHomeScreenState()
    HomeScreenLayout(
        state = state,
        profileIcon = {
            if (email != null) ProfileIcon(
                email = email,
                onClick = onClickUser
            )
        }
    ) { paddingInner, behavior ->
        AnimatedNavHost(
            navController = controller,
            startDestination = startWith
        ) {
            composable(
                route = Route.Movies(),
                deepLinks = Route.Movies.deepLinks
            ) {
                ListingScreen(
                    padding = padding + paddingInner,
                    onClickMovie = onClickMovie,
                    state = moviesState,
                    viewModel = listing,
                    homeState = state,
                    behavior = behavior
                )
            }
            composable(
                route = Route.Cinemas(),
                deepLinks = Route.Cinemas.deepLinks
            ) {
                CinemasScreen(
                    padding = padding + paddingInner,
                    onClickCinema = onClickCinema,
                    viewModel = cinemas,
                    state = cinemasState,
                    homeState = state,
                    behavior = behavior
                )
            }
            composable(
                route = Route.Tickets(),
                deepLinks = Route.Tickets.deepLinks
            ) {
                BookingScreen(
                    padding = padding + paddingInner,
                    viewModel = booking,
                    state = bookingState,
                    onMovieClick = { onClickMovie(it, true) },
                    homeState = state,
                    behavior = behavior
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun HomeScreenLayout(
    state: HomeScreenState = rememberHomeScreenState(),
    profileIcon: @Composable () -> Unit,
    content: @Composable (PaddingValues, TopAppBarScrollBehavior) -> Unit
) {
    val behavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    LaunchedEffect(state.title) {
        behavior.state.contentOffset = 0f
        behavior.state.heightOffset = 0f
    }
    Scaffold(
        topBar = {
            AppToolbar(
                modifier = Modifier.background(Theme.color.container.background.copy(alpha = .9f)),
                title = {
                    if (state.title != 0) AnimatedContent(
                        targetState = stringResource(state.title),
                        transitionSpec = {
                            fadeIn() + slideInHorizontally { it } with fadeOut() + slideOutHorizontally { -it }
                        }
                    ) {
                        Text(it)
                    }
                },
                navigationIcon = profileIcon,
                scrollBehavior = behavior
            )
        },
        content = { content(it, behavior) }
    )
}

@Stable
class HomeScreenState {

    var title by mutableStateOf(0)

}

@Composable
fun rememberHomeScreenState() = remember {
    HomeScreenState()
}

@Composable
private fun ProfileIcon(
    email: String,
    onClick: () -> Unit
) {
    AppIconButton(onClick = onClick) {
        val image by rememberUserImage(email)
        AppImage(
            modifier = Modifier.clip(CircleShape),
            url = image,
            placeholder = painterResource(id = R.drawable.ic_profile)
        )
    }
}

@Composable
fun rememberUserImage(email: String): State<String> {
    val url = remember { mutableStateOf("") }
    LaunchedEffect(key1 = email) {
        val digest = withContext(Dispatchers.Default) {
            MessageDigest.getInstance("MD5")
                .digest(email.lowercase().encodeToByteArray())
                .joinToString("") { "%02x".format(it) }
        }
        url.value = "https://www.gravatar.com/avatar/$digest"
    }
    return url
}

@OptIn(ExperimentalMaterial3Api::class)
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

@Composable
fun <T> RowScope.SelectableNavigationBarItem(
    selected: T,
    index: T,
    icon: Int,
    label: String,
    onSelected: (T) -> Unit,
) {
    NavigationBarItem(
        selected = selected == index,
        onClick = { onSelected(index) },
        colors = NavigationBarItemDefaults.colors(),
        icon = {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = null
            )
        },
        label = { Text(label) }
    )
}