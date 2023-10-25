@file:OptIn(ExperimentalMaterial3Api::class)

package movie.metropolis.app.screen

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
import movie.metropolis.app.screen.home.component.rememberScreenState
import movie.metropolis.app.screen.listing.ListingScreen
import movie.metropolis.app.screen.listing.ListingViewModel
import movie.metropolis.app.screen.order.OrderScreen
import movie.metropolis.app.screen.order.complete.OrderCompleteScreen
import movie.metropolis.app.screen.profile.LoginScreen
import movie.metropolis.app.screen.profile.UserScreen
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
            route = Route.Setup(),
            deepLinks = Route.Setup.deepLinks
        ) {
            SetupScreen(
                viewModel = setupViewModel,
                onNavigateHome = {
                    controller.popBackStack(Route.Setup(), true)
                    controller.navigate(Route.Home.destination())
                }
            )
        }
        composable(
            route = Route.Home(),
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
                    val profileIcon = @Composable {
                        if (email != null) ProfileIcon(
                            email = email,
                            onClick = { controller.navigate(Route.User.destination()) }
                        )
                    }
                    HomeScreen(
                        loggedIn = email != null,
                        listing = { padding ->
                            ListingScreen(
                                behavior = listing.behavior,
                                state = listing.list,
                                viewModel = listing.viewModel,
                                profileIcon = profileIcon,
                                onClickMovie = { id, upcoming ->
                                    controller.navigate(Route.Movie.destination(id, upcoming))
                                },
                                contentPadding = padding
                            )
                        },
                        cinemas = { padding ->
                            CinemasScreen(
                                behavior = cinemas.behavior,
                                state = cinemas.list,
                                viewModel = cinemas.viewModel,
                                profileIcon = profileIcon,
                                onClickCinema = { id ->
                                    controller.navigate(Route.Cinema.destination(id))
                                },
                                contentPadding = padding
                            )
                        },
                        booking = { padding ->
                            BookingScreen(
                                behavior = booking.behavior,
                                viewModel = booking.viewModel,
                                profileIcon = profileIcon,
                                onMovieClick = { id ->
                                    controller.navigate(Route.Movie.destination(id, true))
                                },
                                contentPadding = padding
                            )
                        },
                        settings = { padding ->
                            SettingsScreen(
                                behavior = settings.behavior,
                                viewModel = settings.viewModel,
                                profileIcon = profileIcon,
                                contentPadding = padding
                            )
                        },
                        startWith = args.screen,
                        onClickLogin = { controller.navigate(Route.Login.destination()) },
                        controller = homeController
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

fun NavGraphBuilder.login(
    controller: NavHostController
) = composable(
    route = Route.Login(),
    deepLinks = Route.Login.deepLinks
) {
    LoginScreen(
        onNavigateHome = {
            controller.popBackStack(Route.Home(), true)
            controller.navigate(Route.Home.destination())
        },
        onBackClick = controller::navigateUp
    )
}

fun NavGraphBuilder.user(
    controller: NavHostController
) = composable(
    route = Route.User(),
    deepLinks = Route.User.deepLinks
) {
    UserScreen(
        onNavigateBack = controller::navigateUp
    )
}

fun NavGraphBuilder.cinema(
    controller: NavHostController
) = composable(
    route = Route.Cinema(),
    arguments = Route.Cinema.arguments,
    deepLinks = Route.Cinema.deepLinks
) {
    CinemaScreen(
        onBackClick = controller::navigateUp,
        onBookingClick = { url -> controller.navigate(Route.Order.destination(url)) }
    )
}

fun NavGraphBuilder.movie(
    controller: NavHostController
) = composable(
    route = Route.Movie(),
    arguments = Route.Movie.arguments,
    deepLinks = Route.Movie.deepLinks
) {
    MovieScreen(
        onBackClick = controller::navigateUp,
        onBookingClick = { url -> controller.navigate(Route.Order.destination(url)) }
    )
}

fun NavGraphBuilder.order(
    controller: NavHostController
) = composable(
    route = Route.Order(),
    arguments = Route.Order.arguments,
    deepLinks = Route.Order.deepLinks
) {
    OrderScreen(
        onBackClick = controller::navigateUp,
        onCompleted = {
            controller.popBackStack(Route.Order(), true)
            controller.navigate(Route.OrderComplete.destination())
        }
    )
}

fun NavGraphBuilder.orderComplete(
    controller: NavHostController
) = composable(
    route = Route.OrderComplete(),
    deepLinks = Route.OrderComplete.deepLinks
) {
    OrderCompleteScreen(
        onBackClick = {
            controller.popBackStack(Route.Home(), true)
            controller.navigate(Route.Home.destination(Route.Tickets.destination()))
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