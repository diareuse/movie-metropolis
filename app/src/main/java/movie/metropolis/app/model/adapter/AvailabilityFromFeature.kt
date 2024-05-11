package movie.metropolis.app.model.adapter

import movie.cinema.city.Occurrence
import movie.metropolis.app.model.AvailabilityView
import java.text.DateFormat
import java.util.Date

data class AvailabilityFromFeature(
    private val showing: Occurrence
) : AvailabilityView {

    private val timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT)

    override val id: String
        get() = showing.id
    override val url: String
        get() = showing.booking.toString()
    override val startsAt: String
        get() = timeFormat.format(showing.startsAt)
    override val isEnabled: Boolean
        get() = Date().before(showing.startsAt)

}