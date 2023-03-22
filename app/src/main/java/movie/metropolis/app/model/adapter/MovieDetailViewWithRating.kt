package movie.metropolis.app.model.adapter

import movie.metropolis.app.model.MovieDetailView
import movie.rating.internal.ComposedRating

class MovieDetailViewWithRating(
    private val origin: MovieDetailView,
    private val ratings: ComposedRating
) : MovieDetailView by origin {

    override val rating: String?
        get() = ratings.max?.value?.let { "%d%%".format(it) }
    override val links: MovieDetailView.Links
        get() = Links()

    private inner class Links : MovieDetailView.Links {
        override val imdb: String?
            get() = ratings.imdb?.url
        override val csfd: String?
            get() = ratings.csfd?.url
        override val rottenTomatoes: String?
            get() = ratings.rottenTomatoes?.url
    }

}