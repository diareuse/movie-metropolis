package movie.metropolis.app.ui

import android.content.Intent
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.platform.*
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import movie.metropolis.app.screen.Route
import movie.metropolis.app.screen.setup.component.rememberOneTapSaving
import movie.metropolis.app.screen.setup.component.requestOneTapAsState
import movie.metropolis.app.ui.booking.BookingScreen
import movie.metropolis.app.ui.booking.BookingViewModel
import movie.metropolis.app.ui.home.HomeScreen
import movie.metropolis.app.ui.home.HomeViewModel
import movie.metropolis.app.ui.movie.MovieScreen
import movie.metropolis.app.ui.movie.MovieViewModel
import movie.metropolis.app.ui.profile.ProfileScreen
import movie.metropolis.app.ui.profile.ProfileViewModel
import movie.metropolis.app.ui.setup.SetupScreen
import movie.metropolis.app.ui.setup.SetupViewModel

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun Navigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    SharedTransitionLayout(modifier = modifier) {
        val setupVm = hiltViewModel<SetupViewModel>()
        LaunchedEffect(setupVm) {
            setupVm.requiresSetup.collect {
                val route = if (it) Route.Setup() else Route.Home()
                navController.navigate(route) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        inclusive = true
                    }
                }
            }
        }
        NavHost(
            modifier = Modifier.fillMaxSize(),
            navController = navController,
            startDestination = Route.None.route
        ) {
            composable(Route.None.route) { Text("None") }
            composable(Route.Setup.route) {
                val scope = rememberCoroutineScope()
                val saving = rememberOneTapSaving()
                val oneTap by requestOneTapAsState()
                LaunchedEffect(oneTap) {
                    val ot = oneTap
                    if (ot != null) {
                        setupVm.state.email = ot.userName
                        setupVm.state.password = ot.password.concatToString()
                    }
                }
                fun navigateHome() {
                    navController.navigate(Route.Home()) {
                        popUpTo(Route.Setup.route) {
                            inclusive = true
                        }
                    }
                }
                SetupScreen(
                    state = setupVm.state,
                    onLoginClick = {
                        scope.launch {
                            if (!setupVm.login().await()) {
                                return@launch
                            }
                            if (
                                oneTap?.userName != setupVm.state.email ||
                                oneTap?.password?.concatToString() != setupVm.state.password
                            ) {
                                saving.save(setupVm.state.email, setupVm.state.password)
                            }
                            navigateHome()
                        }
                    },
                    onExitClick = ::navigateHome
                )
            }
            composable(Route.Home.route, arguments = Route.Home.arguments) {
                val vm = hiltViewModel<HomeViewModel>()
                HomeScreen(
                    state = vm.state,
                    onMovieClick = { id, upcoming ->
                        navController.navigate(Route.Movie(id, upcoming))
                    },
                    onProfileClick = {
                        navController.navigate(Route.User())
                    },
                    onCinemaClick = {
                        navController.navigate(Route.Booking.Cinema(it))
                    }
                )
            }
            composable(Route.User.route) {
                val vm = hiltViewModel<ProfileViewModel>()
                ProfileScreen(
                    state = vm.state,
                    onBackClick = navController::navigateUp,
                    onPhoneChange = vm::onPhoneChange,
                    onFirstNameChange = vm::onFirstNameChange,
                    onLastNameChange = vm::onLastNameChange,
                    onConsentChange = vm::onConsentChange
                )
            }
            composable(Route.UserEditor.route) { Text("UserEditor") }
            composable(Route.Movie.route, arguments = Route.Movie.arguments) {
                val vm = hiltViewModel<MovieViewModel>()
                val state = vm.state
                val context = LocalContext.current
                MovieScreen(
                    showPurchase = !vm.upcoming,
                    detail = state.detail,
                    onBackClick = navController::navigateUp,
                    onLinkClick = {
                        var intent = Intent(Intent.ACTION_VIEW).setData(it.toUri())
                        intent = Intent.createChooser(intent, it)
                        context.startActivity(intent)
                    },
                    onBuyClick = { navController.navigate(Route.Booking.Movie(state.detail.id)) }
                )
            }
            composable(Route.Booking.Movie.route) {
                val vm = hiltViewModel<BookingViewModel>()
                BookingScreen(
                    state = vm.state,
                    onBackClick = navController::navigateUp,
                    onTimeClick = { navController.navigate(Route.Order(it)) }
                )
            }
            composable(Route.Booking.Cinema.route) {
                val vm = hiltViewModel<BookingViewModel>()
                BookingScreen(
                    state = vm.state,
                    onBackClick = navController::navigateUp,
                    onTimeClick = { navController.navigate(Route.Order(it)) }
                )
            }
            composable(Route.OrderComplete.route) { Text("OrderComplete") }
            composable(Route.Order.route) { Text("Order") }
            composable(Route.Settings.route) { Text("Settings") }
        }
    }
}