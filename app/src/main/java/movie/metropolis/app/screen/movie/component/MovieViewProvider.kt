package movie.metropolis.app.screen.movie.component

import androidx.compose.ui.tooling.preview.datasource.*
import movie.metropolis.app.model.MovieView

class MovieViewProvider :
    CollectionPreviewParameterProvider<MovieView>(List(10) {
        MovieView(it.toString()).apply {
            name = "Movie $it"
        }
    })