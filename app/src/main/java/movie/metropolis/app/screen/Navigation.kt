package movie.metropolis.app.screen

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import movie.metropolis.app.screen.booking.BookingScreen
import movie.metropolis.app.screen.cinema.CinemaScreen
import movie.metropolis.app.screen.cinema.CinemasScreen
import movie.metropolis.app.screen.detail.MovieScreen
import movie.metropolis.app.screen.listing.MoviesScreen
import movie.metropolis.app.screen.profile.LoginScreen
import movie.metropolis.app.screen.profile.UserScreen

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun Navigation(
    controller: NavHostController = rememberAnimatedNavController()
) {
    AnimatedNavHost(
        navController = controller,
        startDestination = "/movies"
    ) {
        composable("/user/login") {
            LoginScreen()
        }
        composable("/user") {
            UserScreen()
        }
        composable("/user/booking") {
            BookingScreen()
        }
        composable("/cinemas") {
            CinemasScreen()
        }
        composable("/cinemas/{cinema}") {
            CinemaScreen()
        }
        composable("/movies") {
            MoviesScreen()
        }
        composable("/movies/{movie}") {
            MovieScreen()
        }
    }
}