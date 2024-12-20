package movie.metropolis.app.ui.home

import androidx.compose.runtime.*
import movie.metropolis.app.model.CinemaView
import movie.metropolis.app.model.MovieView

@Stable
class HomeScreenState {
    val current = mutableStateListOf<MovieView>()
    val upcoming = mutableStateListOf<MovieView>()
    val cinemas = mutableStateListOf<CinemaView>()
}