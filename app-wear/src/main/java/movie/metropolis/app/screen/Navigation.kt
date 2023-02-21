package movie.metropolis.app.screen

import androidx.compose.animation.*
import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import movie.metropolis.app.screen.booking.BookingsScreen
import movie.metropolis.app.screen.home.HomeScreen

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun Navigation(
    navController: NavHostController = rememberAnimatedNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Route.Tickets()
    ) {
        composable(route = Route.Home()) {
            HomeScreen()
        }

        composable(route = Route.Tickets()) {
            BookingsScreen(onTicketClick = {
                navController.navigate(Route.Ticket.destination(it))
            })
        }

        composable(
            route = Route.Ticket(),
            arguments = Route.Ticket.arguments
        ) {

        }
    }
}

