@file:OptIn(
    ExperimentalPermissionsApi::class, ExperimentalFoundationApi::class,
    ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class
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
import androidx.compose.material.icons.*
import androidx.compose.material.icons.automirrored.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.res.*
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
import movie.metropolis.app.R
import movie.metropolis.app.feature.location.rememberLocation
import movie.metropolis.app.model.Calendars
import movie.metropolis.app.model.CinemaView
import movie.metropolis.app.screen.Route
import movie.metropolis.app.screen2.booking.BookingFiltersDialog
import movie.metropolis.app.screen2.booking.BookingScreen
import movie.metropolis.app.screen2.booking.TimeViewModel
import movie.metropolis.app.screen2.cinema.CinemasScreen
import movie.metropolis.app.screen2.cinema.CinemasViewModel
import movie.metropolis.app.screen2.home.HomeScreen
import movie.metropolis.app.screen2.home.HomeState
import movie.metropolis.app.screen2.home.HomeViewModel
import movie.metropolis.app.screen2.listing.ListingScreen
import movie.metropolis.app.screen2.listing.ListingViewModel
import movie.metropolis.app.screen2.movie.MovieScreen
import movie.metropolis.app.screen2.movie.MovieViewModel
import movie.metropolis.app.screen2.profile.ProfileScreen
import movie.metropolis.app.screen2.purchase.PurchaseCompleteScreen
import movie.metropolis.app.screen2.purchase.PurchaseCompleteViewModel
import movie.metropolis.app.screen2.purchase.PurchaseScreen
import movie.metropolis.app.screen2.purchase.PurchaseViewModel
import movie.metropolis.app.screen2.purchase.component.Confetti
import movie.metropolis.app.screen2.settings.SettingsScreen
import movie.metropolis.app.screen2.settings.SettingsViewModel
import movie.metropolis.app.screen2.settings.component.CalendarColumn
import movie.metropolis.app.screen2.settings.component.CalendarDialog
import movie.metropolis.app.screen2.setup.SetupScreen
import movie.metropolis.app.screen2.setup.SetupState
import movie.metropolis.app.screen2.setup.SetupViewModel
import movie.metropolis.app.screen2.ticket.TicketScreen
import movie.metropolis.app.screen2.ticket.TicketViewModel
import movie.style.CollapsingTopAppBar
import movie.style.Container
import movie.style.DialogBox
import movie.style.action.actionView

@Composable
fun Navigation(
    navController: NavHostController = rememberNavController()
) {
    val setupViewModel = hiltViewModel<SetupViewModel>()
    LaunchedEffect(setupViewModel) {
        setupViewModel.requiresSetup.collect {
            if (it) navController.navigate(Route.Setup()) {
                popUpTo(Route.Home.route) {
                    inclusive = true
                }
            }
        }
    }
    NavHost(
        modifier = Modifier.fillMaxSize(),
        navController = navController,
        startDestination = Route.Home.route,
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
        upcoming(navController)
    }
}

fun NavGraphBuilder.upcoming(navController: NavHostController) = composable(
    route = Route.Upcoming.route,
    deepLinks = Route.Upcoming.deepLinks
) {
    val listingVM = hiltViewModel<ListingViewModel>()
    val promotions by listingVM.promotions.collectAsState()
    val movies by listingVM.movies.collectAsState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    Scaffold(
        topBar = {
            CollapsingTopAppBar(
                scrollBehavior = scrollBehavior,
                title = { Text(stringResource(R.string.upcoming)) },
                navigationIcon = {
                    IconButton(onClick = navController::navigateUp) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, null)
                    }
                }
            )
        }
    ) { padding ->
        ListingScreen(
            modifier = Modifier.fillMaxSize(),
            contentPadding = padding,
            state = rememberLazyStaggeredGridState(),
            movies = movies.toImmutableList(),
            promotions = promotions.toImmutableList(),
            onClick = { navController.navigate(Route.Movie(it.id, true)) },
            onFavoriteClick = { listingVM.favorite(it) },
            onMoreClick = null,
            connection = scrollBehavior.nestedScrollConnection
        )
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
    val args = remember(it) { Route.Home.Arguments(it) }
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
        startWith = args.screen?.let(HomeState.Companion::by),
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
                onFavoriteClick = { listingVM.favorite(it) },
                onMoreClick = { navController.navigate(Route.Upcoming()) }
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
    val args = remember(it) { Route.Movie.Arguments(it) }
    val viewModel = hiltViewModel<MovieViewModel>()
    val movie by viewModel.movie.collectAsState()
    val onBookClick = if (args.upcoming) null else ({
        val m = movie
        if (m != null)
            navController.navigate(Route.Booking.Movie(m.id))
    })
    MovieScreen(
        movie = movie,
        onBackClick = navController::navigateUp,
        onBookClick = onBookClick
    )
}

private fun NavGraphBuilder.order(
    navController: NavHostController
) = composable(
    route = Route.Order.route,
    arguments = Route.Order.arguments,
    deepLinks = Route.Order.deepLinks
) {
    val viewModel = hiltViewModel<PurchaseViewModel>()
    val request by viewModel.request.collectAsState()
    val isCompleted by viewModel.isCompleted.collectAsState()
    LaunchedEffect(isCompleted) {
        if (!isCompleted) return@LaunchedEffect
        navController.navigate(Route.OrderComplete())
    }
    PurchaseScreen(
        request = request,
        onUrlChanged = viewModel::updateUrl,
        onBackClick = navController::navigateUp
    )
}

private fun NavGraphBuilder.orderComplete(
    navController: NavHostController
) = composable(
    route = Route.OrderComplete.route,
    deepLinks = Route.OrderComplete.deepLinks
) {
    val viewModel = hiltViewModel<PurchaseCompleteViewModel>()
    val products by viewModel.products.collectAsState()
    Confetti {
        PurchaseCompleteScreen(
            products = products,
            onBackClick = {
                navController.navigate(Route.Home(HomeState.Tickets)) {
                    popUpTo(Route.Home.route) {
                        inclusive = true
                    }
                }
            }
        )
    }
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
        val poster by viewModel.poster.collectAsState()
        val title by viewModel.title.collectAsState()
        val filters by viewModel.filters.collectAsState()
        var filtersVisible by rememberSaveable { mutableStateOf(false) }
        val location by rememberLocation()
        LaunchedEffect(location) {
            viewModel.location.value = location
        }
        DialogBox(
            visible = filtersVisible,
            dialog = {
                Container(
                    modifier = Modifier.fillMaxWidth(),
                    onDismissRequest = { filtersVisible = false }
                ) {
                    BookingFiltersDialog(
                        filters = filters,
                        onLanguageClick = viewModel::toggle,
                        onTypeClick = viewModel::toggle
                    )
                }
            }
        ) {
            BookingScreen(
                poster = poster,
                title = title,
                items = times.toImmutableList(),
                onBackClick = navController::navigateUp,
                onTimeClick = { navController.navigate(Route.Order(it.url)) },
                onActionClick = { filtersVisible = true }
            )
        }
    }
    composable(
        route = Route.Booking.Cinema.route,
        deepLinks = Route.Booking.Cinema.deepLinks
    ) {
        val viewModel = hiltViewModel<TimeViewModel>()
        val times by viewModel.times.collectAsState()
        val poster by viewModel.poster.collectAsState()
        val title by viewModel.title.collectAsState()
        val filters by viewModel.filters.collectAsState()
        var filtersVisible by rememberSaveable { mutableStateOf(false) }
        DialogBox(
            visible = filtersVisible,
            dialog = {
                Container(
                    modifier = Modifier.fillMaxWidth(),
                    onDismissRequest = { filtersVisible = false }
                ) {
                    BookingFiltersDialog(
                        filters = filters,
                        onLanguageClick = viewModel::toggle,
                        onTypeClick = viewModel::toggle
                    )
                }
            }
        ) {
            BookingScreen(
                poster = poster,
                title = title,
                items = times.toImmutableList(),
                onBackClick = navController::navigateUp,
                onTimeClick = { navController.navigate(Route.Order(it.url)) },
                onActionClick = {}
            )
        }
    }
}