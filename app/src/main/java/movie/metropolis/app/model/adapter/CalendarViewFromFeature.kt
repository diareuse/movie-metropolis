package movie.metropolis.app.model.adapter

import movie.calendar.CalendarMetadata
import movie.metropolis.app.model.CalendarView

class CalendarViewFromFeature(
    private val calendar: CalendarMetadata
) : CalendarView {

    override val id: String
        get() = calendar.id
    override val name: String
        get() = calendar.name
    override val account: String
        get() = calendar.account

}