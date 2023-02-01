package movie.metropolis.app.screen

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import movie.metropolis.app.screen.cinema.CinemaScreen
import movie.metropolis.app.screen.detail.MovieScreen
import movie.metropolis.app.screen.home.HomeScreen
import movie.metropolis.app.screen.order.OrderScreen
import movie.metropolis.app.screen.profile.LoginScreen
import movie.metropolis.app.screen.profile.UserScreen
import movie.metropolis.app.screen.settings.SettingsScreen
import movie.metropolis.app.screen.setup.SetupScreen
import movie.metropolis.app.screen.setup.SetupViewModel
import movie.metropolis.app.util.encodeBase64

private const val uri = "app://movie.metropolis"

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun Navigation(
    controller: NavHostController = rememberAnimatedNavController()
) {
    val setupViewModel = hiltViewModel<SetupViewModel>()
    val requiresSetup by setupViewModel.requiresSetup.collectAsState()
    AnimatedNavHost(
        navController = controller,
        startDestination = if (requiresSetup) "/setup" else "/home",
        enterTransition = { slideInHorizontally { it } },
        exitTransition = { fadeOut() + slideOutHorizontally() },
        popEnterTransition = { fadeIn() + slideInHorizontally() },
        popExitTransition = { slideOutHorizontally { it } }
    ) {
        composable("/setup") {
            SetupScreen(
                viewModel = setupViewModel,
                onNavigateHome = {
                    controller.popBackStack("/setup", true)
                    controller.navigate("/home")
                }
            )
        }
        composable(
            route = "/home",
            arguments = listOf(navArgument("screen") { defaultValue = "movies" }),
            deepLinks = listOf(navDeepLink { uriPattern = "$uri/home?screen={screen}" })
        ) {
            HomeScreen(
                startWith = it.arguments?.getString("screen"),
                onClickMovie = { id, upcoming ->
                    controller.navigate("/movies/${id}?upcoming=$upcoming")
                },
                onClickCinema = { id -> controller.navigate("/cinemas/$id") },
                onClickUser = { controller.navigate("/user") },
                onClickLogin = { controller.navigate("/user/login") }
            )
        }
        composable("/user") {
            UserScreen(
                onNavigateToSettings = { controller.navigate("/user/settings") },
                onNavigateBack = controller::navigateUp
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
                onBookingClick = { url ->
                    controller.navigate("/order/${url.encodeBase64()}")
                }
            )
        }
        composable(
            route = "/movies/{movie}?upcoming={upcoming}",
            deepLinks = listOf(navDeepLink { uriPattern = "$uri/movies/{movie}" })
        ) {
            MovieScreen(
                onBackClick = controller::navigateUp,
                onBookingClick = { url ->
                    controller.navigate("/order/${url.encodeBase64()}")
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