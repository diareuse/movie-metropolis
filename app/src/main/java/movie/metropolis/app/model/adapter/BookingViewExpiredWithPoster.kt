package movie.metropolis.app.model.adapter

import movie.metropolis.app.model.BookingView
import movie.metropolis.app.model.ImageView
import movie.metropolis.app.model.MovieDetailView

class BookingViewExpiredWithPoster(
    private val origin: BookingView.Expired,
    private val poster: ImageView
) : BookingView.Expired by origin {

    override val movie: MovieDetailView
        get() = MovieDetailViewWithPoster(origin.movie, poster)

}