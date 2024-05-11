package movie.metropolis.app.model.adapter

import movie.metropolis.app.model.AvailabilityView
import movie.metropolis.app.model.CinemaBookingView
import movie.metropolis.app.model.CinemaView

data class CinemaBookingViewFilter(
    private val languages: Set<String>,
    private val types: Set<String>,
    private val origin: CinemaBookingView
) : CinemaBookingView {

    override val cinema: CinemaView
        get() = origin.cinema
    override val availability: Map<AvailabilityView.Type, List<AvailabilityView>>
        get() = origin.availability.filterKeys {
            (it.language in languages) and (types.isEmpty() || it.types.containsAll(types))
        }

}