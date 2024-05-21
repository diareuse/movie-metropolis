package movie.metropolis.app.model.adapter

import movie.cinema.city.Cinema
import movie.cinema.city.Occurrence
import movie.metropolis.app.model.AvailabilityView
import movie.metropolis.app.model.CinemaBookingView
import movie.metropolis.app.model.CinemaView
import java.util.Date

data class CinemaBookingViewFromCinema(
    private val _cinema: Cinema,
    private val _occurrences: List<Occurrence>
) : CinemaBookingView {
    override val cinema: CinemaView
        get() = _cinema.let(::CinemaViewFromCinema)
    override val availability: Map<AvailabilityView.Type, List<AvailabilityView>>
        get() = _occurrences.groupBy(
            keySelector = {
                Type(
                    it.flags.map { it.tag },
                    it.dubbing.toString() + "/" + (it.subtitles?.toString() ?: "-")
                )
            },
            valueTransform = {
                Availability(it)
            }
        )

    private data class Availability(
        private val occurrence: Occurrence
    ) : AvailabilityView {
        override val id: String
            get() = occurrence.id
        override val url: String
            get() = occurrence.booking.toString()
        override val startsAt: String
            get() = occurrence.startsAt.toString()
        override val isEnabled: Boolean
            get() = Date().before(occurrence.startsAt)
    }

    private data class Type(
        override val types: List<String>,
        override val language: String
    ) : AvailabilityView.Type
}