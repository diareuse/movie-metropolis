package movie.metropolis.app.ui.home

import androidx.compose.runtime.*
import movie.metropolis.app.model.CinemaView
import movie.metropolis.app.model.MovieView
import movie.metropolis.app.ui.profile.ProfileState
import movie.metropolis.app.ui.ticket.TicketScreenState

@Stable
class HomeScreenState {
    val profile = ProfileState()
    val recommended = mutableStateListOf<MovieView>()
    val comingSoon = mutableStateListOf<MovieView>()
    val cinemas = mutableStateListOf<CinemaView>()
    val tickets = TicketScreenState()
}