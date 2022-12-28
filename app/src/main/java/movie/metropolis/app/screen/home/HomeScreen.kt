package movie.metropolis.app.screen.home

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import movie.metropolis.app.R
import movie.metropolis.app.feature.image.imageRequestOf
import movie.metropolis.app.screen.booking.BookingScreen
import movie.metropolis.app.screen.booking.BookingViewModel
import movie.metropolis.app.screen.cinema.CinemasScreen
import movie.metropolis.app.screen.cinema.CinemasViewModel
import movie.metropolis.app.screen.listing.ListingViewModel
import movie.metropolis.app.screen.listing.MoviesScreen
import java.security.MessageDigest

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HomeScreen(
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
    onClickMovie: (String, Boolean) -> Unit,
    onClickCinema: (String) -> Unit,
    onClickUser: () -> Unit,
    onClickLogin: () -> Unit
) {
    val email = viewModel.email
    HomeScreen(
        isLoggedIn = email != null,
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
                onMovieClick = { onClickMovie(it, false) },
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
            CenterAlignedTopAppBar(
                title = title,
                navigationIcon = profileIcon,
                scrollBehavior = behavior,
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent,
                    scrolledContainerColor = Color.Transparent
                )
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
    IconButton(onClick = onClick) {
        var isSuccess by remember { mutableStateOf(false) }
        var size by remember { mutableStateOf(IntSize.Zero) }
        val filter = if (isSuccess) null else ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
        AsyncImage(
            modifier = Modifier
                .clip(CircleShape)
                .onGloballyPositioned { size = it.size },
            model = imageRequestOf(rememberUserImage(email).value, size),
            contentDescription = null,
            placeholder = painterResource(id = R.drawable.ic_profile),
            error = painterResource(id = R.drawable.ic_profile),
            fallback = painterResource(id = R.drawable.ic_profile),
            colorFilter = filter,
            onSuccess = { isSuccess = true },
            contentScale = ContentScale.Crop
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
    movies: @Composable (PaddingValues) -> Unit,
    cinemas: @Composable (PaddingValues) -> Unit,
    booking: @Composable (PaddingValues) -> Unit,
    onNavigateToLogin: () -> Unit
) {
    val (selected, onChanged) = rememberSaveable { mutableStateOf(0) }
    Scaffold(
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets
            .only(WindowInsetsSides.Bottom + WindowInsetsSides.Horizontal),
        bottomBar = {
            Surface(
                tonalElevation = 1.dp,
                shadowElevation = 32.dp,
                color = MaterialTheme.colorScheme.surface
            ) {
                NavigationBar(
                    containerColor = Color.Transparent
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
                        onSelected = {
                            if (isLoggedIn) onChanged(it)
                            else onNavigateToLogin()
                        }
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