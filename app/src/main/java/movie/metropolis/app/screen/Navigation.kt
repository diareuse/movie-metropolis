package movie.metropolis.app.screen

import androidx.compose.animation.*
import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import movie.metropolis.app.screen.cinema.CinemaScreen
import movie.metropolis.app.screen.detail.MovieScreen
import movie.metropolis.app.screen.home.HomeScreen
import movie.metropolis.app.screen.order.OrderScreen
import movie.metropolis.app.screen.order.complete.OrderCompleteScreen
import movie.metropolis.app.screen.profile.LoginScreen
import movie.metropolis.app.screen.profile.UserScreen
import movie.metropolis.app.screen.setup.SetupScreen
import movie.metropolis.app.screen.setup.SetupViewModel

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun Navigation(
    controller: NavHostController = rememberAnimatedNavController()
) {
    val setupViewModel = hiltViewModel<SetupViewModel>()
    val requiresSetup by setupViewModel.requiresSetup.collectAsState()
    AnimatedNavHost(
        navController = controller,
        startDestination = if (requiresSetup) Route.Setup() else Route.Home(),
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
            HomeScreen(
                startWith = args.screen,
                onClickMovie = { id, upcoming ->
                    controller.navigate(Route.Movie.destination(id, upcoming))
                },
                onClickCinema = { id -> controller.navigate(Route.Cinema.destination(id)) },
                onClickUser = { controller.navigate(Route.User.destination()) },
                onClickLogin = { controller.navigate(Route.Login.destination()) }
            )
        }
        composable(
            route = Route.User(),
            deepLinks = Route.User.deepLinks
        ) {
            UserScreen(
                onNavigateBack = controller::navigateUp
            )
        }
        composable(
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
        composable(
            route = Route.Cinema(),
            arguments = Route.Cinema.arguments,
            deepLinks = Route.Cinema.deepLinks
        ) {
            CinemaScreen(
                onBackClick = controller::navigateUp,
                onBookingClick = { url -> controller.navigate(Route.Order.destination(url)) }
            )
        }
        composable(
            route = Route.Movie(),
            arguments = Route.Movie.arguments,
            deepLinks = Route.Movie.deepLinks
        ) {
            MovieScreen(
                onBackClick = controller::navigateUp,
                onBookingClick = { url -> controller.navigate(Route.Order.destination(url)) }
            )
        }
        composable(
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
        composable(
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
    }
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