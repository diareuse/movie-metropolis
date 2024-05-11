package movie.metropolis.app.model.adapter

import movie.cinema.city.Movie
import movie.metropolis.app.model.BookingView
import movie.metropolis.app.model.MovieDetailView

class BookingViewWithDetail(
    private val origin: BookingView,
    private val detail: Movie
) : BookingView by origin {

    override val movie: MovieDetailView
        get() = MovieDetailViewFromFeature(detail)

}