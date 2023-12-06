package movie.core

import movie.core.model.Cinema
import movie.core.model.Location
import movie.core.preference.SyncPreference
import java.util.Date

class EventCinemaFeatureSaveTimestamp(
    private val origin: EventCinemaFeature,
    private val preference: SyncPreference
) : EventCinemaFeature {

    override suspend fun get(location: Location?): Sequence<Cinema> {
        return origin.get(location).also { preference.cinema = Date() }
    }

}