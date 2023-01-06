package movie.metropolis.app.screen

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.navigation.navDeepLink
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import movie.metropolis.app.screen.cinema.CinemaScreen
import movie.metropolis.app.screen.detail.MovieScreen
import movie.metropolis.app.screen.home.HomeScreen
import movie.metropolis.app.screen.order.OrderScreen
import movie.metropolis.app.screen.profile.LoginScreen
import movie.metropolis.app.screen.profile.UserScreen
import movie.metropolis.app.screen.settings.SettingsScreen
import movie.metropolis.app.util.encodeBase64
import java.io.File

private const val uri = "app://movie.metropolis"

@OptIn(ExperimentalAnimationApi::class, ExperimentalPagerApi::class)
@Composable
fun Navigation(
    onPermissionsRequested: suspend (Array<String>) -> Boolean,
    onLinkClicked: (String) -> Unit,
    onShareFile: (File) -> Unit,
    controller: NavHostController = rememberAnimatedNavController()
) {
    AnimatedNavHost(
        navController = controller,
        startDestination = "/home"
    ) {
        composable("/home?screen={screen}") {
            HomeScreen(
                startWith = it.arguments?.getString("screen"),
                onPermissionsRequested = onPermissionsRequested,
                onShareFile = onShareFile,
                onClickMovie = { id, upcoming ->
                    controller.navigate("/movies/${id}?upcoming=$upcoming")
                },
                onClickCinema = { id -> controller.navigate("/cinemas/$id") },
                onClickUser = { controller.navigate("/user") },
                onClickLogin = { controller.navigate("/user/login") }
            )
        }
        composable("/home") {
            LaunchedEffect(Unit) {
                controller.popBackStack("/home", true)
                controller.navigate("/home?screen=movies")
            }
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
                onPermissionsRequested = onPermissionsRequested,
                onLinkClick = onLinkClicked,
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
                onPermissionsRequested = onPermissionsRequested,
                onBackClick = controller::navigateUp
            )
        }
    }
}

