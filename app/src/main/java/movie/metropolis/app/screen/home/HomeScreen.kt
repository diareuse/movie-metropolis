package movie.metropolis.app.screen.home

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import movie.metropolis.app.R
import movie.metropolis.app.screen.booking.BookingScreen
import movie.metropolis.app.screen.booking.BookingViewModel
import movie.metropolis.app.screen.cinema.CinemasScreen
import movie.metropolis.app.screen.cinema.CinemasViewModel
import movie.metropolis.app.screen.listing.ListingViewModel
import movie.metropolis.app.screen.listing.MoviesScreen
import movie.style.AppButton
import movie.style.AppIconButton
import movie.style.AppImage
import movie.style.AppToolbar
import movie.style.haptic.ClickOnChange
import movie.style.theme.Theme
import java.io.File
import java.security.MessageDigest

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HomeScreen(
    startWith: String? = null,
    viewModel: HomeViewModel = hiltViewModel(),
    listing: ListingViewModel = hiltViewModel(),
    cinemas: CinemasViewModel = hiltViewModel(),
    booking: BookingViewModel = hiltViewModel(),
    moviesState: LazyListState = rememberLazyListState(),
    moviesAvailableState: PagerState = rememberPagerState(),
    moviesUpcomingState: LazyListState = rememberLazyListState(),
    cinemasState: LazyListState = rememberLazyListState(),
    bookingState: LazyListState = rememberLazyListState(),
    onPermissionsRequested: suspend (Array<String>) -> Boolean,
    onShareFile: (File) -> Unit,
    onClickMovie: (String, Boolean) -> Unit,
    onClickCinema: (String) -> Unit,
    onClickUser: () -> Unit,
    onClickLogin: () -> Unit
) {
    val email = viewModel.email
    HomeScreen(
        isLoggedIn = email != null,
        initialScreen = startWith,
        movies = {
            MoviesScreen(
                padding = it,
                onClickMovie = onClickMovie,
                state = moviesState,
                stateAvailable = moviesAvailableState,
                stateUpcoming = moviesUpcomingState,
                viewModel = listing,
                onPermissionsRequested = onPermissionsRequested,
                profileIcon = {
                    if (email != null) ProfileIcon(
                        email = email,
                        onClick = onClickUser
                    )
                }
            )
        },
        cinemas = {
            CinemasScreen(
                padding = it,
                onPermissionRequested = onPermissionsRequested,
                onClickCinema = onClickCinema,
                viewModel = cinemas,
                state = cinemasState,
                profileIcon = {
                    if (email != null) ProfileIcon(
                        email = email,
                        onClick = onClickUser
                    )
                }
            )
        },
        booking = {
            BookingScreen(
                padding = it,
                viewModel = booking,
                state = bookingState,
                onPermissionsRequested = onPermissionsRequested,
                onMovieClick = { onClickMovie(it, false) },
                onShareFile = onShareFile,
                profileIcon = {
                    if (email != null) ProfileIcon(
                        email = email,
                        onClick = onClickUser
                    )
                }
            )
        },
        onNavigateToLogin = onClickLogin
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenLayout(
    profileIcon: @Composable () -> Unit,
    title: @Composable () -> Unit,
    content: @Composable (PaddingValues, TopAppBarScrollBehavior) -> Unit
) {
    val behavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    Scaffold(
        topBar = {
            AppToolbar(
                title = title,
                navigationIcon = profileIcon,
                scrollBehavior = behavior
            )
        },
        content = { content(it, behavior) }
    )
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
    initialScreen: String?,
    movies: @Composable (PaddingValues) -> Unit,
    cinemas: @Composable (PaddingValues) -> Unit,
    booking: @Composable (PaddingValues) -> Unit,
    onNavigateToLogin: () -> Unit
) {
    val (selected, onChanged) = rememberSaveable {
        mutableStateOf(
            when (initialScreen) {
                "movies" -> 0
                "cinemas" -> 1
                "tickets" -> 2
                else -> 0
            }
        )
    }
    ClickOnChange(selected)
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
                        selected = selected,
                        index = 0,
                        icon = R.drawable.ic_movie,
                        label = stringResource(R.string.movies),
                        onSelected = onChanged
                    )
                    SelectableNavigationBarItem(
                        selected = selected,
                        index = 1,
                        icon = R.drawable.ic_cinema,
                        label = stringResource(R.string.cinemas),
                        onSelected = onChanged
                    )
                    SelectableNavigationBarItem(
                        selected = selected,
                        index = 2,
                        icon = R.drawable.ic_ticket,
                        label = stringResource(R.string.tickets),
                        onSelected = onChanged
                    )
                }
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
    onSelected: (Int) -> Unit,
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