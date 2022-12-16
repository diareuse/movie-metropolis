package movie.metropolis.app.model.adapter

import movie.core.model.Showing
import movie.metropolis.app.model.AvailabilityView
import java.text.DateFormat
import java.util.Date

data class AvailabilityFromFeature(
    private val showing: Showing
) : AvailabilityView {

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