@file:OptIn(
    ExperimentalPermissionsApi::class, ExperimentalFoundationApi::class,
    ExperimentalMaterial3Api::class
)

package movie.metropolis.app.screen2

import android.Manifest
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.lazy.staggeredgrid.*
import androidx.compose.foundation.pager.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.unit.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch
import movie.metropolis.app.feature.location.rememberLocation
import movie.metropolis.app.model.Calendars
import movie.metropolis.app.model.CinemaView
import movie.metropolis.app.screen.Route
import movie.metropolis.app.screen2.booking.BookingScreen
import movie.metropolis.app.screen2.booking.TimeViewModel
import movie.metropolis.app.screen2.cinema.CinemasScreen
import movie.metropolis.app.screen2.cinema.CinemasViewModel
import movie.metropolis.app.screen2.home.HomeScreen
import movie.metropolis.app.screen2.home.HomeViewModel
import movie.metropolis.app.screen2.listing.ListingScreen
import movie.metropolis.app.screen2.listing.ListingViewModel
import movie.metropolis.app.screen2.movie.MovieScreen
import movie.metropolis.app.screen2.movie.MovieViewModel
import movie.metropolis.app.screen2.profile.ProfileScreen
import movie.metropolis.app.screen2.settings.SettingsScreen
import movie.metropolis.app.screen2.settings.SettingsViewModel
import movie.metropolis.app.screen2.settings.component.CalendarColumn
import movie.metropolis.app.screen2.settings.component.CalendarDialog
import movie.metropolis.app.screen2.setup.SetupScreen
import movie.metropolis.app.screen2.setup.SetupState
import movie.metropolis.app.screen2.setup.SetupViewModel
import movie.metropolis.app.screen2.ticket.TicketScreen
import movie.metropolis.app.screen2.ticket.TicketViewModel
import movie.style.action.actionView
import java.net.URL

@Composable
fun Navigation(
    navController: NavHostController = rememberNavController()
) {
    val setupViewModel = hiltViewModel<SetupViewModel>()
    LaunchedEffect(setupViewModel) {
        setupViewModel.requiresSetup.collect {
            if (it) navController.navigate(Route.Setup()) {
                popUpTo(Route.Home()) {
                    inclusive = true
                }
            }
        }
    }
    NavHost(
        modifier = Modifier.fillMaxSize(),
        navController = navController,
        startDestination = Route.Home(),
        enterTransition = { slideInHorizontally { it } },
        exitTransition = { fadeOut() + slideOutHorizontally() },
        popEnterTransition = { fadeIn() + slideInHorizontally() },
        popExitTransition = { slideOutHorizontally { it } }
    ) {
        setup(navController)
        home(navController)
        settings(navController)
        movie(navController)
        order(navController)
        orderComplete(navController)
        booking(navController)
    }
}

private fun NavGraphBuilder.setup(
    navController: NavHostController
) = composable(
    route = Route.Setup.route,
    deepLinks = Route.Setup.deepLinks
) {
    val viewModel = hiltViewModel<SetupViewModel>()
    val regions by viewModel.regions.collectAsState()
    val posters = viewModel.posters.toImmutableList()
    val requiresSetup by viewModel.requiresSetup.collectAsState(initial = true)
    val loginState by viewModel.loginState.collectAsState()
    val scope = rememberCoroutineScope()
    val navigateHome = {
        navController.navigate(Route.Home()) {
            popUpTo(Route.Setup.route) {
                inclusive = true
            }
        }
    }
    SetupScreen(
        startWith = it.arguments?.getString("startWith")?.let(SetupState::valueOf)
            ?: SetupState.Initial,
        regions = regions.getOrNull().orEmpty().toImmutableList(),
        posters = posters,
        regionSelected = !requiresSetup,
        onRegionClick = viewModel::select,
        loginState = loginState,
        onLoginStateChange = { viewModel.loginState.value = it },
        onLoginClick = {
            scope.launch {
                viewModel.login().onSuccess {
                    navigateHome()
                }
            }
        },
        onLoginSkip = navigateHome
    )
}

private fun NavGraphBuilder.home(
    navController: NavHostController
) = composable(
    route = Route.Home.route,
    arguments = Route.Home.arguments,
    deepLinks = Route.Home.deepLinks
) {
    val viewModel = hiltViewModel<HomeViewModel>()
    val listingVM = hiltViewModel<ListingViewModel>()
    val cinemasVM = hiltViewModel<CinemasViewModel>()
    val bookingVM = hiltViewModel<TicketViewModel>()
    val listingState = rememberLazyStaggeredGridState()
    val cinemasState = rememberLazyListState()
    val tickets by bookingVM.tickets.collectAsState()
    val bookingState = rememberPagerState { tickets.size }
    val user by viewModel.user.collectAsState()
    val membership by viewModel.membership.collectAsState()
    var showCard by remember { mutableStateOf(false) }
    HomeScreen(
        loggedIn = viewModel.isLoggedIn,
        user = user,
        membership = membership,
        showProfile = showCard,
        onShowProfileChange = { showCard = it },
        onNavigateToLogin = { navController.navigate(Route.Setup(SetupState.Login)) },
        listing = { modifier, padding ->
            val promotions by listingVM.promotions.collectAsState()
            val movies by listingVM.movies.collectAsState()
            ListingScreen(
                modifier = modifier,
                contentPadding = padding,
                state = listingState,
                movies = movies.toImmutableList(),
                promotions = promotions.toImmutableList(),
                onClick = { navController.navigate(Route.Movie(it.id)) },
                onFavoriteClick = { listingVM.favorite(it) }
            )
        },
        tickets = { modifier, padding ->
            LaunchedEffect(Unit) {
                bookingVM.refresh()
            }
            TicketScreen(
                bookings = tickets.toImmutableList(),
                state = bookingState,
                modifier = modifier,
                contentPadding = padding
            )
        },
        cinemas = { modifier, padding ->
            val state = rememberMultiplePermissionsState(
                listOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
            val location by rememberLocation(state)
            val cinemas by cinemasVM.cinemas.collectAsState()
            LaunchedEffect(location) {
                cinemasVM.location.value = location
            }
            CinemasScreen(
                modifier = modifier,
                contentPadding = padding,
                cinemas = cinemas.toImmutableList(),
                permission = state,
                state = cinemasState,
                onClick = { navController.navigate(Route.Booking.Cinema(it.id)) },
                onMapClick = actionView<CinemaView> { it.uri }
            )
        },
        profile = { modifier, padding ->
            ProfileScreen(
                user = user,
                modifier = modifier,
                contentPadding = padding,
                onClickCard = { showCard = true },
                onClickEdit = {},
                onClickFavorite = {},
                onClickSettings = { navController.navigate(Route.Settings()) }
            )
        }
    )
}

private fun NavGraphBuilder.settings(
    navController: NavHostController
) = composable(
    route = Route.Settings.route,
    deepLinks = Route.Settings.deepLinks
) {
    val viewModel = hiltViewModel<SettingsViewModel>()
    val state by viewModel.state.collectAsState()
    val calendars by viewModel.calendars.collectAsState(Calendars())
    var showCalendars by remember { mutableStateOf(false) }
    val permission = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.READ_CALENDAR,
            Manifest.permission.WRITE_CALENDAR
        )
    )
    val blur by animateDpAsState(targetValue = if (showCalendars) 32.dp else 0.dp)
    SettingsScreen(
        modifier = Modifier.blur(blur),
        state = state,
        onStateChange = viewModel::updateState,
        onClickBack = navController::navigateUp,
        onShowCalendarsRequest = { showCalendars = true }
    )
    if (showCalendars) {
        LaunchedEffect(permission.allPermissionsGranted) {
            if (!permission.allPermissionsGranted) {
                permission.launchMultiplePermissionRequest()
            } else {
                viewModel.refreshCalendars()
            }
        }
        if (permission.allPermissionsGranted) CalendarDialog(
            onDismissRequest = {
                showCalendars = false
                viewModel.updateState(state.copy(selectedCalendar = null))
            }
        ) {
            CalendarColumn(
                modifier = Modifier.fillMaxWidth(),
                calendars = calendars,
                onCalendarSelect = {
                    showCalendars = false
                    viewModel.updateState(state.copy(selectedCalendar = it))
                }
            )
        }
    }
}

private fun NavGraphBuilder.movie(
    navController: NavHostController
) = composable(
    route = Route.Movie.route,
    arguments = Route.Movie.arguments,
    deepLinks = Route.Movie.deepLinks
) {
    val viewModel = hiltViewModel<MovieViewModel>()
    val movie by viewModel.movie.collectAsState()
    MovieScreen(movie = movie, onBackClick = navController::navigateUp, onBookClick = {
        val m = movie
        if (m != null)
            navController.navigate(Route.Booking.Movie(m.id))
    })
}

fun a() {
    URL("").openStream()
}

private fun NavGraphBuilder.order(
    navController: NavHostController
) = composable(
    route = Route.Order.route,
    arguments = Route.Order.arguments,
    deepLinks = Route.Order.deepLinks
) {
}

private fun NavGraphBuilder.orderComplete(
    navController: NavHostController
) = composable(
    route = Route.OrderComplete.route,
    deepLinks = Route.OrderComplete.deepLinks
) {
}

private fun NavGraphBuilder.booking(
    navController: NavHostController
) {
    composable(
        route = Route.Booking.Movie.route,
        deepLinks = Route.Booking.Movie.deepLinks
    ) {
        val viewModel = hiltViewModel<TimeViewModel>()
        val times by viewModel.times.collectAsState()
        BookingScreen(times.toImmutableList())
    }
    composable(
        route = Route.Booking.Cinema.route,
        deepLinks = Route.Booking.Cinema.deepLinks
    ) {
        val viewModel = hiltViewModel<TimeViewModel>()
        val times by viewModel.times.collectAsState()
        BookingScreen(times.toImmutableList())
    }
}