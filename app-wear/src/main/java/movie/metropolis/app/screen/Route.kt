package movie.metropolis.app.screen

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

}