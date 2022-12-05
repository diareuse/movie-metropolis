package movie.metropolis.app.model.adapter

import movie.core.model.Cinema
import movie.core.model.Showing
import movie.metropolis.app.model.CinemaBookingView
import movie.metropolis.app.model.CinemaView
import java.text.DateFormat
import java.util.Date

data class CinemaBookingViewFromFeature(
    private val location: Cinema,
    private val booking: Iterable<Showing>
) : CinemaBookingView {
    override val cinema: CinemaView
        get() = CinemaViewFromFeature(location)
    override val availability: Map<CinemaBookingView.LanguageAndType, List<CinemaBookingView.Availability>>
        get() = booking.groupBy(CinemaBookingViewFromFeature::LanguageAndTypeFromFeature)
            .mapValues { (_, values) -> values.map(CinemaBookingViewFromFeature::AvailabilityFromFeature) }

    private class LanguageAndTypeFromFeature(
        private val item: Showing
    ) : CinemaBookingView.LanguageAndType {
        override val language: String get() = item.language
        override val type: String get() = item.type

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is LanguageAndTypeFromFeature) return false

            if (language != other.language) return false
            if (type != other.type) return false

            return true
        }

        override fun hashCode(): Int {
            var result = language.hashCode()
            result = 31 * result + type.hashCode()
            return result
        }

    }

    private data class AvailabilityFromFeature(
        private val showing: Showing
    ) : CinemaBookingView.Availability {

        private val timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT)

        override val id: String
            get() = showing.id
        override val url: String
            get() = showing.bookingUrl
        override val startsAt: String
            get() = timeFormat.format(showing.startsAt)
        override val isEnabled: Boolean
            get() = showing.isEnabled && Date().before(showing.startsAt)

    }

}