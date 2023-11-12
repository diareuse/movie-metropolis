package movie.metropolis.app.model.adapter

import movie.metropolis.app.model.MovieDetailView
import movie.rating.MovieMetadata

class MovieDetailViewWithRating(
    private val origin: MovieDetailView,
    private val ratings: MovieMetadata?
) : MovieDetailView by origin {

    override val rating: String?
        get() = ratings?.rating?.takeIf { it > 0 }?.let { "%d%%".format(it) }

}