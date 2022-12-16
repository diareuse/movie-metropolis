package movie.metropolis.app.model.adapter

import movie.metropolis.app.model.AvailabilityView
import movie.metropolis.app.model.MovieBookingView

data class MovieBookingViewFilter(
    private val filter: Set<String>,
    private val origin: MovieBookingView
) : MovieBookingView {

    override val movie: MovieBookingView.Movie
        get() = origin.movie
    override val availability: Map<AvailabilityView.Type, List<AvailabilityView>>
        get() = origin.availability.filterKeys { it.type in filter && it.language in filter }

}