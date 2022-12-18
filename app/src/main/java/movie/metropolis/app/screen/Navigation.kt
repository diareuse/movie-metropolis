package movie.metropolis.app.screen

import android.util.Base64
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.navDeepLink
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import movie.metropolis.app.R
import movie.metropolis.app.screen.booking.BookingScreen
import movie.metropolis.app.screen.booking.BookingViewModel
import movie.metropolis.app.screen.cinema.CinemaScreen
import movie.metropolis.app.screen.cinema.CinemasScreen
import movie.metropolis.app.screen.cinema.CinemasViewModel
import movie.metropolis.app.screen.detail.MovieScreen
import movie.metropolis.app.screen.listing.ListingViewModel
import movie.metropolis.app.screen.listing.MoviesScreen
import movie.metropolis.app.screen.order.OrderScreen
import movie.metropolis.app.screen.profile.LoginScreen
import movie.metropolis.app.screen.profile.ProfileViewModel
import movie.metropolis.app.screen.profile.UserScreen
import movie.metropolis.app.screen.settings.SettingsScreen

private const val uri = "app://movie.metropolis"

@OptIn(ExperimentalAnimationApi::class, ExperimentalPagerApi::class)
@Composable
fun Navigation(
    onPermissionsRequested: suspend (Array<String>) -> Boolean,
    onLinkClicked: (String) -> Unit,
    controller: NavHostController = rememberAnimatedNavController()
) {
    AnimatedNavHost(
        navController = controller,
        startDestination = "/home"
    ) {
        composable("/home") {
            val listing: ListingViewModel = hiltViewModel()
            val cinemas: CinemasViewModel = hiltViewModel()
            val booking: BookingViewModel = hiltViewModel()
            val user: ProfileViewModel = hiltViewModel()
            val moviesState = rememberLazyListState()
            val moviesAvailableState = rememberPagerState()
            val moviesUpcomingState = rememberLazyListState()
            val cinemasState = rememberLazyListState()
            val bookingState = rememberLazyListState()
            val userState = rememberScrollState()
            HomeScreen(
                movies = {
                    MoviesScreen(
                        padding = it,
                        onClickMovie = { id, upcoming -> controller.navigate("/movies/${id}?upcoming=$upcoming") },
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
                        onClickCinema = { id -> controller.navigate("/cinemas/$id") },
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
                },
                user = {
                    UserScreen(
                        padding = it,
                        onNavigateToLogin = { controller.navigate("/user/login") },
                        onNavigateToSettings = { controller.navigate("/user/settings") },
                        viewModel = user,
                        state = userState
                    )
                }
            )
        }
        composable("/user/login") {
            LoginScreen(
                onNavigateHome = {
                    controller.popBackStack("/home", true)
                    controller.navigate("/home")
                },
                onBackClick = controller::navigateUp
            )
        }
        composable("/cinemas/{cinema}") {
            CinemaScreen(
                onBackClick = controller::navigateUp,
                onBookingClick = {
                    val url = Base64.encodeToString(
                        it.encodeToByteArray(),
                        Base64.URL_SAFE or Base64.NO_PADDING
                    )
                    controller.navigate("/order/$url")
                }
            )
        }
        composable(
            route = "/movies/{movie}?upcoming={upcoming}",
            deepLinks = listOf(navDeepLink { uriPattern = "$uri/movies/{movie}" })
        ) {
            MovieScreen(
                onBackClick = controller::navigateUp,
                onPermissionsRequested = onPermissionsRequested,
                onVideoClick = onLinkClicked,
                onBookingClick = {
                    val url = Base64.encodeToString(
                        it.encodeToByteArray(),
                        Base64.URL_SAFE or Base64.NO_PADDING
                    )
                    controller.navigate("/order/$url")
                }
            )
        }
        composable("/order/{url}") {
            OrderScreen(
                onBackClick = controller::navigateUp
            )
        }
        composable("/user/settings") {
            SettingsScreen(
                onBackClick = controller::navigateUp
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    movies: @Composable (PaddingValues) -> Unit,
    cinemas: @Composable (PaddingValues) -> Unit,
    booking: @Composable (PaddingValues) -> Unit,
    user: @Composable (PaddingValues) -> Unit,
) {
    val (selected, onChanged) = rememberSaveable { mutableStateOf(0) }
    Scaffold(
        topBar = {
            Column {
                CenterAlignedTopAppBar(
                    title = {
                        val text = when (selected) {
                            0 -> "Movies"
                            1 -> "Cinemas"
                            2 -> "Tickets"
                            3 -> "You"
                            else -> ""
                        }
                        Text(text)
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
                        scrolledContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
                    )
                )
            }
        },
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
                SelectableNavigationBarItem(
                    selected = selected,
                    index = 3,
                    icon = R.drawable.ic_user,
                    label = "You",
                    onSelected = onChanged
                )
            }
        }
    ) {
        when (selected) {
            0 -> movies(it)
            1 -> cinemas(it)
            2 -> booking(it)
            3 -> user(it)
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