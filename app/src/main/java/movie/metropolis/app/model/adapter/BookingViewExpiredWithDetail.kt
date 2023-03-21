package movie.metropolis.app.model.adapter

import movie.core.model.MovieDetail
import movie.metropolis.app.model.BookingView
import movie.metropolis.app.model.MovieDetailView

class BookingViewExpiredWithDetail(
    private val origin: BookingView.Expired,
    private val detail: MovieDetail
) : BookingView.Expired by origin {

    override val movie: MovieDetailView
        get() = MovieDetailViewFromFeature(detail)

}