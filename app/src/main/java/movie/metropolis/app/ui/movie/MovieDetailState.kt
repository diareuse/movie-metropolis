package movie.metropolis.app.ui.movie

import androidx.compose.runtime.*
import movie.metropolis.app.model.MovieDetailView

@Stable
class MovieDetailState {
    var detail by mutableStateOf(MovieDetailView(""))
}