package movie.metropolis.app.screen

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class Route(
    val route: String
) {

    operator fun invoke() = route

    object Home : Route("home") {
        fun destination() = route
    }

    object Tickets : Route("bookings") {
        fun destination() = route
    }

    object Ticket : Route("bookings/{booking}") {
        val arguments = listOf(
            navArgument("booking") {
                type = NavType.StringType
                nullable = false
            }
        )

        class Arguments(private val entry: SavedStateHandle) {
            val id get() = entry.get<String>("booking")
        }

        fun destination(id: String) =
            route.replace("{booking}", id)
    }

}