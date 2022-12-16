package movie.metropolis.app.model.adapter

import movie.metropolis.app.model.AvailabilityView
import movie.metropolis.app.model.MovieBookingView
import movie.metropolis.app.screen.cinema.ShowingFilterable

data class MovieBookingViewFilter(
    private val filter: Set<String>,
    private val origin: MovieBookingView
) : MovieBookingView {

    constructor(
        filterable: ShowingFilterable,
        origin: MovieBookingView
    ) : this(
        filterable.getSelectedTags(),
        origin
    )

    override val movie: MovieBookingView.Movie
        get() = origin.movie
    override val availability: Map<AvailabilityView.Type, List<AvailabilityView>>
        get() = origin.availability.filterKeys {
            var hasHit = it.language in filter
            for (type in it.types)
                hasHit = hasHit and (type in filter)
            hasHit
        }

}