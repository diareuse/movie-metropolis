package movie.metropolis.app.model.adapter

import movie.core.model.MovieDetail
import movie.metropolis.app.model.BookingView
import movie.metropolis.app.model.MovieDetailView

class BookingViewActiveWithDetail(
    private val origin: BookingView.Active,
    private val detail: MovieDetail
) : BookingView.Active by origin {

    override val movie: MovieDetailView
        get() = MovieDetailViewFromFeature(detail)

}