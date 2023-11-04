package movie.metropolis.app.model

import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.toImmutableMap

class Calendars(
    calendars: List<CalendarView> = emptyList()
) : ImmutableMap<String, List<CalendarView>> by (calendars.groupBy { it.account }.toImmutableMap())