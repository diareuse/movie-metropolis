package movie.core

import movie.core.util.requireNotEmpty
import java.util.Date

class EventShowingsFeatureCinemaRequireNotEmpty(
    private val origin: EventShowingsFeature.Cinema
) : EventShowingsFeature.Cinema {

    override suspend fun get(date: Date) = origin.get(date)
        .mapCatching { it.requireNotEmpty() }

}

