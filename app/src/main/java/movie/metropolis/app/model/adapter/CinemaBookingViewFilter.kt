package movie.metropolis.app.model.adapter

import movie.metropolis.app.model.AvailabilityView
import movie.metropolis.app.model.CinemaBookingView
import movie.metropolis.app.model.CinemaView

data class CinemaBookingViewFilter(
    private val filter: Set<String>,
    private val origin: CinemaBookingView
) : CinemaBookingView {

    override val cinema: CinemaView
        get() = origin.cinema
    override val availability: Map<AvailabilityView.Type, List<AvailabilityView>>
        get() = origin.availability.filterKeys {
            var hasHit = it.language in filter
            for (type in it.types)
                hasHit = hasHit and (type in filter)
            hasHit
        }

}