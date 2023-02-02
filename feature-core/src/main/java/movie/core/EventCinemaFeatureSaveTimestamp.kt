package movie.core

import kotlinx.coroutines.coroutineScope
import movie.core.model.Cinema
import movie.core.model.Location
import movie.core.preference.SyncPreference
import java.util.Date

class EventCinemaFeatureSaveTimestamp(
    private val origin: EventCinemaFeature,
    private val preference: SyncPreference
) : EventCinemaFeature {

    override suspend fun get(
        location: Location?,
        result: ResultCallback<Iterable<Cinema>>
    ) = coroutineScope {
        origin.get(location, result.then(this) {
            preference.cinema = Date()
        })
    }

}