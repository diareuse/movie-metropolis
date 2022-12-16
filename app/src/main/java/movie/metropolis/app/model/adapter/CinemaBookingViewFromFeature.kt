package movie.metropolis.app.model.adapter

import movie.core.model.Cinema
import movie.core.model.Showing
import movie.metropolis.app.model.AvailabilityView
import movie.metropolis.app.model.CinemaBookingView
import movie.metropolis.app.model.CinemaView

data class CinemaBookingViewFromFeature(
    private val location: Cinema,
    private val booking: Iterable<Showing>
) : CinemaBookingView {
    override val cinema: CinemaView
        get() = CinemaViewFromFeature(location)
    override val availability: Map<AvailabilityView.Type, List<AvailabilityView>>
        get() = booking.groupBy(::LanguageAndTypeFromFeature)
            .mapValues { (_, values) -> values.map(::AvailabilityFromFeature) }

}