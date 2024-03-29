package movie.metropolis.app.model.adapter

import movie.core.model.Showing
import movie.metropolis.app.model.SpecificTimeView
import java.text.DateFormat
import java.util.Date

data class SpecificTimeViewFromFeature(
    private val showing: Showing
) : SpecificTimeView {
    override val time: Long
        get() = showing.startsAt.time
    override val formatted: String
        get() = timeFormat.format(showing.startsAt.time)
    override val url: String
        get() = showing.bookingUrl
    override val isEnabled: Boolean
        get() = showing.isEnabled && Date().before(showing.startsAt)

    companion object {
        private val timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT)
    }
}