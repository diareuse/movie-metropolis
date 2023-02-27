package movie.metropolis.app.screen.listing

import androidx.compose.ui.tooling.preview.datasource.*
import movie.metropolis.app.model.MovieView

class ListMovieViewProvider : CollectionPreviewParameterProvider<List<MovieView>>(buildList {
    val provider = MovieViewProvider()
    this += provider.values.toList()
})