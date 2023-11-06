package movie.metropolis.app.model.adapter

import movie.core.model.Showing
import movie.metropolis.app.model.SpecificTimeView
import java.text.DateFormat

data class SpecificTimeViewFromFeature(
    private val showing: Showing
) : SpecificTimeView {
    override val time: Long
        get() = showing.startsAt.time
    override val formatted: String
        get() = timeFormat.format(showing.startsAt.time)

    companion object {
        private val timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT)
    }
}