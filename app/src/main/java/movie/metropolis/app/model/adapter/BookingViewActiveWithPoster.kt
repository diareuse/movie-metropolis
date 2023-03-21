package movie.metropolis.app.model.adapter

import movie.metropolis.app.model.BookingView
import movie.metropolis.app.model.ImageView
import movie.metropolis.app.model.MovieDetailView

class BookingViewActiveWithPoster(
    private val origin: BookingView.Active,
    private val poster: ImageView
) : BookingView.Active by origin {

    override val movie: MovieDetailView
        get() = MovieDetailViewWithPoster(origin.movie, poster)

}