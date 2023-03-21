package movie.metropolis.app.model.adapter

import movie.metropolis.app.model.ImageView
import movie.metropolis.app.model.MovieDetailView

class MovieDetailViewWithPoster(
    private val origin: MovieDetailView,
    override val poster: ImageView
) : MovieDetailView by origin