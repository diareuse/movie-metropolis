package movie.metropolis.app.model

import androidx.compose.runtime.*
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.toImmutableMap

@Immutable
class Calendars(
    calendars: List<CalendarView> = emptyList()
) : ImmutableMap<String, List<CalendarView>> by (calendars.groupBy { it.account }.toImmutableMap())