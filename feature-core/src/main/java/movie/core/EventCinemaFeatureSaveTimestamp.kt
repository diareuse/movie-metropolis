package movie.core

import movie.core.model.Cinema
import movie.core.model.Location
import movie.core.preference.SyncPreference
import java.util.Date

class EventCinemaFeatureSaveTimestamp(
    private val origin: EventCinemaFeature,
    private val preference: SyncPreference
) : EventCinemaFeature {

    override suspend fun get(location: Location?, result: ResultCallback<Iterable<Cinema>>) {
        origin.get(location) {
            result(it)
            it.onSuccess {
                preference.cinema = Date()
            }
        }
    }

}