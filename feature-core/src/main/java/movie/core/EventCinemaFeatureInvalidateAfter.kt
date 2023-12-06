package movie.core

import movie.core.model.Cinema
import movie.core.model.Location
import movie.core.preference.SyncPreference
import movie.core.preference.SyncPreference.Companion.isInThreshold
import kotlin.time.Duration

class EventCinemaFeatureInvalidateAfter(
    private val origin: EventCinemaFeature,
    private val preference: SyncPreference,
    private val duration: Duration
) : EventCinemaFeature {

    override suspend fun get(location: Location?): Sequence<Cinema> {
        val lastRefresh = preference.cinema
        if (!lastRefresh.isInThreshold(duration)) {
            throw ExpiredException(lastRefresh, duration)
        }
        return origin.get(location)
    }

}