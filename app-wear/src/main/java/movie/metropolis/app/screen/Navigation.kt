package movie.metropolis.app.screen

import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import movie.metropolis.app.screen.booking.BookingScreen
import movie.metropolis.app.screen.booking.BookingsScreen

@Composable
fun Navigation(
    navController: NavHostController = rememberSwipeDismissableNavController()
) {
    SwipeDismissableNavHost(
        navController = navController,
        startDestination = Route.Tickets()
    ) {
        composable(route = Route.Tickets()) {
            BookingsScreen(onTicketClick = {
                navController.navigate(Route.Ticket.destination(it))
            })
        }

        composable(
            route = Route.Ticket(),
            arguments = Route.Ticket.arguments
        ) {
            BookingScreen()
        }
    }
}

