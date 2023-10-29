@file:OptIn(ExperimentalMaterial3Api::class)

package movie.metropolis.app.screen

import android.Manifest
import android.os.Build
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.res.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.unit.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import movie.metropolis.app.LocalActivityActions
import movie.metropolis.app.R
import movie.metropolis.app.screen.booking.BookingScreen
import movie.metropolis.app.screen.booking.BookingViewModel
import movie.metropolis.app.screen.cinema.CinemaScreen
import movie.metropolis.app.screen.cinema.CinemasScreen
import movie.metropolis.app.screen.cinema.CinemasViewModel
import movie.metropolis.app.screen.detail.MovieScreen
import movie.metropolis.app.screen.home.HomeScreen
import movie.metropolis.app.screen.home.HomeViewModel
import movie.metropolis.app.screen.home.component.ProfileIcon
import movie.metropolis.app.screen.home.component.rememberInstantApp
import movie.metropolis.app.screen.home.component.rememberScreenState
import movie.metropolis.app.screen.listing.ListingScreen
import movie.metropolis.app.screen.listing.ListingViewModel
import movie.metropolis.app.screen.order.OrderScreen
import movie.metropolis.app.screen.order.complete.OrderCompleteScreen
import movie.metropolis.app.screen.profile.LoginScreen
import movie.metropolis.app.screen.profile.LoginViewModel
import movie.metropolis.app.screen.profile.UserScreen
import movie.metropolis.app.screen.profile.component.rememberOneTapSaving
import movie.metropolis.app.screen.profile.component.requestOneTapAsState
import movie.metropolis.app.screen.settings.SettingsScreen
import movie.metropolis.app.screen.settings.SettingsViewModel
import movie.metropolis.app.screen.setup.SetupScreen
import movie.metropolis.app.screen.setup.SetupViewModel
import movie.style.TwoPaneSurface
import movie.style.theme.Theme

@Composable
fun Navigation(
    controller: NavHostController = rememberNavController()
) {
    val setupViewModel = hiltViewModel<SetupViewModel>()
    LaunchedEffect(setupViewModel) {
        setupViewModel.requiresSetup.collect {
            if (it) controller.navigate(Route.Setup()) {
                popUpTo(Route.Home()) {
                    inclusive = true
                }
            }
        }
    }
    NavHost(
        navController = controller,
        startDestination = Route.Home(),
        enterTransition = { slideInHorizontally { it } },
        exitTransition = { fadeOut() + slideOutHorizontally() },
        popEnterTransition = { fadeIn() + slideInHorizontally() },
        popExitTransition = { slideOutHorizontally { it } }
    ) {
        composable(
            route = Route.Setup.route,
            deepLinks = Route.Setup.deepLinks
        ) {
            SetupScreen(
                viewModel = setupViewModel,
                onNavigateHome = {
                    controller.popBackStack(Route.Setup.route, true)
                    controller.navigate(Route.Home())
                }
            )
        }
        composable(
            route = Route.Home.route,
            arguments = Route.Home.arguments,
            deepLinks = Route.Home.deepLinks
        ) {
            val args = remember(it) { Route.Home.Arguments(it) }
            val viewModel = hiltViewModel<HomeViewModel>()
            val email = viewModel.email
            var controller = controller
            val homeController = rememberNavController()

            TwoPaneSurface(
                primary = {
                    val listing = rememberScreenState<ListingViewModel>()
                    val cinemas = rememberScreenState<CinemasViewModel>()
                    val booking = rememberScreenState<BookingViewModel>()
                    val settings = rememberScreenState<SettingsViewModel>()
                    val profileIcon: (@Composable () -> Unit)? = if (email != null) ({
                        ProfileIcon(
                            email = email,
                            onClick = { controller.navigate(Route.User()) }
                        )
                    }) else null
                    val instantApp = rememberInstantApp()
                    HomeScreen(
                        loggedIn = email != null,
                        instantApp = instantApp.isInstant,
                        listing = { padding ->
                            val promotions by listing.viewModel.promotions.collectAsState()
                            val groups by listing.viewModel.groups.collectAsState()
                            val selectedType by listing.viewModel.selectedType.collectAsState()
                            val scope = rememberCoroutineScope()
                            val actions = LocalActivityActions.current
                            ListingScreen(
                                promotions = promotions,
                                groups = groups,
                                selectedType = selectedType,
                                onSelectedTypeChange = {
                                    listing.viewModel.selectedType.value = it
                                },
                                onClickFavorite = {
                                    scope.launch {
                                        val granted =
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                                actions.requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS))
                                            } else {
                                                true
                                            }
                                        if (!granted) return@launch
                                        listing.viewModel.toggleFavorite(it)
                                    }
                                },
                                onClick = { id, upcoming ->
                                    controller.navigate(Route.Movie(id, upcoming))
                                },
                                contentPadding = padding,
                                behavior = listing.behavior,
                                state = listing.list,
                            )
                        },
                        cinemas = { padding ->
                            CinemasScreen(
                                behavior = cinemas.behavior,
                                state = cinemas.list,
                                viewModel = cinemas.viewModel,
                                onClickCinema = { id ->
                                    controller.navigate(Route.Cinema(id))
                                },
                                contentPadding = padding
                            )
                        },
                        booking = { padding ->
                            BookingScreen(
                                behavior = booking.behavior,
                                viewModel = booking.viewModel,
                                onMovieClick = { id ->
                                    controller.navigate(Route.Movie(id, true))
                                },
                                contentPadding = padding
                            )
                        },
                        settings = { padding ->
                            SettingsScreen(
                                behavior = settings.behavior,
                                viewModel = settings.viewModel,
                                contentPadding = padding
                            )
                        },
                        startWith = args.screen,
                        onClickLogin = { controller.navigate(Route.Login()) },
                        onClickInstall = instantApp::install,
                        controller = homeController,
                        profileIcon = profileIcon
                    )
                },
                secondary = {
                    controller = rememberNavController()
                    val destination by homeController.currentDestinationAsState()
                    LaunchedEffect(destination) {
                        while (controller.currentBackStackEntry?.destination?.route != Route.Home.route)
                            if (!controller.popBackStack()) break
                    }
                    NavHost(
                        navController = controller,
                        startDestination = Route.Home.route
                    ) {
                        composable(
                            route = Route.Home.route,
                            arguments = Route.Home.arguments,
                            deepLinks = Route.Home.deepLinks
                        ) {
                            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Text(
                                    modifier = Modifier.padding(64.dp),
                                    text = stringResource(R.string.select_content_left),
                                    style = Theme.textStyle.title,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                        login(controller)
                        user(controller)
                        cinema(controller)
                        movie(controller)
                        order(controller)
                        orderComplete(controller)
                    }
                }
            )
        }
        login(controller)
        user(controller)
        cinema(controller)
        movie(controller)
        order(controller)
        orderComplete(controller)
    }
}

private fun NavGraphBuilder.login(
    controller: NavHostController
) = composable(
    route = Route.Login.route,
    deepLinks = Route.Login.deepLinks
) {
    val viewModel = hiltViewModel<LoginViewModel>()
    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()
    val domain = remember(viewModel) { viewModel.domain }
    val state by viewModel.loading.collectAsState()
    val mode by viewModel.mode.collectAsState()
    val urls by viewModel.posters.collectAsState()
    val oneTap by requestOneTapAsState()
    val oneTapSave = rememberOneTapSaving()
    val scope = rememberCoroutineScope()

    fun navigateHome() {
        controller.popBackStack(Route.Home.route, true)
        controller.navigate(Route.Home())
    }

    LaunchedEffect(oneTap) {
        val oneTap = oneTap
        if (oneTap != null) {
            viewModel.email.value = oneTap.userName
            viewModel.password.value = oneTap.password.concatToString()
            viewModel.send { navigateHome() }
        }
    }
    LoginScreen(
        email = email,
        password = password,
        error = state.isFailure,
        loading = state.isLoading,
        domain = domain,
        onEmailChanged = { viewModel.email.value = it },
        onPasswordChanged = { viewModel.password.value = it },
        onSendClick = {
            viewModel.send {
                scope.launch {
                    oneTapSave.save(email, password)
                    navigateHome()
                }
            }
        },
        onBackClick = controller::navigateUp,
        mode = mode,
        urls = urls
    )
}

private fun NavGraphBuilder.user(
    controller: NavHostController
) = composable(
    route = Route.User.route,
    deepLinks = Route.User.deepLinks
) {
    UserScreen(
        onNavigateBack = controller::navigateUp
    )
}

private fun NavGraphBuilder.cinema(
    controller: NavHostController
) = composable(
    route = Route.Cinema.route,
    arguments = Route.Cinema.arguments,
    deepLinks = Route.Cinema.deepLinks
) {
    CinemaScreen(
        onBackClick = controller::navigateUp,
        onBookingClick = { url -> controller.navigate(Route.Order(url)) }
    )
}

private fun NavGraphBuilder.movie(
    controller: NavHostController
) = composable(
    route = Route.Movie.route,
    arguments = Route.Movie.arguments,
    deepLinks = Route.Movie.deepLinks
) {
    MovieScreen(
        onBackClick = controller::navigateUp,
        onBookingClick = { url -> controller.navigate(Route.Order(url)) }
    )
}

private fun NavGraphBuilder.order(
    controller: NavHostController
) = composable(
    route = Route.Order.route,
    arguments = Route.Order.arguments,
    deepLinks = Route.Order.deepLinks
) {
    OrderScreen(
        onBackClick = controller::navigateUp,
        onCompleted = {
            controller.popBackStack(Route.Order.route, true)
            controller.navigate(Route.OrderComplete())
        }
    )
}

private fun NavGraphBuilder.orderComplete(
    controller: NavHostController
) = composable(
    route = Route.OrderComplete.route,
    deepLinks = Route.OrderComplete.deepLinks
) {
    OrderCompleteScreen(
        onBackClick = {
            controller.popBackStack(Route.Home.route, true)
            controller.navigate(Route.Home(Route.Tickets()))
        }
    )
}

val NavHostController.currentDestinationFlow
    get() = callbackFlow {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            trySend(destination)
        }
        addOnDestinationChangedListener(listener)
        awaitClose {
            removeOnDestinationChangedListener(listener)
        }
    }

@Composable
fun NavHostController.currentDestinationAsState(): State<NavDestination?> {
    return currentDestinationFlow.collectAsState(currentDestination)
}