package movie.metropolis.app.model.adapter

import movie.core.model.MovieDetail
import movie.metropolis.app.model.BookingView
import movie.metropolis.app.model.MovieDetailView

class BookingViewWithDetail(
    private val origin: BookingView,
    private val detail: MovieDetail
) : BookingView by origin {

    override val movie: MovieDetailView
        get() = MovieDetailViewFromFeature(detail)

}