package movie.metropolis.app.model.adapter

import movie.cinema.city.Occurrence
import movie.metropolis.app.model.SpecificTimeView
import java.text.DateFormat
import java.util.Date

data class SpecificTimeViewFromFeature(
    private val showing: Occurrence
) : SpecificTimeView {
    override val time: Long
        get() = showing.startsAt.time
    override val formatted: String
        get() = timeFormat.format(showing.startsAt.time)
    override val url: String
        get() = showing.booking.toString()
    override val isEnabled: Boolean
        get() = Date().before(showing.startsAt)

    companion object {
        private val timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT)
    }
}