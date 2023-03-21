package movie.metropolis.app.model.adapter

import movie.metropolis.app.model.AvailabilityView
import movie.metropolis.app.model.MovieBookingView
import movie.metropolis.app.presentation.cinema.ShowingFilterable

data class MovieBookingViewFilter(
    private val languages: Set<String>,
    private val types: Set<String>,
    private val origin: MovieBookingView
) : MovieBookingView {

    constructor(
        filterable: ShowingFilterable,
        origin: MovieBookingView
    ) : this(
        filterable.selectedLanguages,
        filterable.selectedTypes,
        origin
    )

    override val movie: MovieBookingView.Movie
        get() = origin.movie
    override val availability: Map<AvailabilityView.Type, List<AvailabilityView>>
        get() = origin.availability.filterKeys {
            (it.language in languages) and (types.isEmpty() || it.types.containsAll(types))
        }

}