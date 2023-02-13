package movie.metropolis.app.model.adapter

import movie.metropolis.app.model.AvailabilityView
import movie.metropolis.app.model.CinemaBookingView
import movie.metropolis.app.model.CinemaView
import movie.metropolis.app.presentation.cinema.ShowingFilterable

data class CinemaBookingViewFilter(
    private val languages: Set<String>,
    private val types: Set<String>,
    private val origin: CinemaBookingView
) : CinemaBookingView {

    constructor(
        filterable: ShowingFilterable,
        origin: CinemaBookingView
    ) : this(
        filterable.getSelectedLanguages(),
        filterable.getSelectedTypes(),
        origin
    )

    override val cinema: CinemaView
        get() = origin.cinema
    override val availability: Map<AvailabilityView.Type, List<AvailabilityView>>
        get() = origin.availability.filterKeys {
            (it.language in languages) and (types.isEmpty() || it.types.containsAll(types))
        }

}