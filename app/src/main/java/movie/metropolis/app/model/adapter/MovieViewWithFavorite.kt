package movie.metropolis.app.model.adapter

import movie.metropolis.app.model.MovieView

data class MovieViewWithFavorite(
    private val origin: MovieView,
    override val favorite: Boolean
) : MovieView by origin