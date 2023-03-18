package movie.core

import java.util.Date

class EventShowingsFeatureCinemaCatch(
    private val origin: EventShowingsFeature.Cinema
) : EventShowingsFeature.Cinema, Recoverable {

    override suspend fun get(date: Date) = wrap { origin.get(date) }

}