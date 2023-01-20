package movie.core

import movie.core.model.Cinema
import movie.core.model.Location
import movie.core.util.requireNotEmpty

class EventCinemaFeatureRequireNotEmpty(
    private val origin: EventCinemaFeature
) : EventCinemaFeature {

    override suspend fun get(location: Location?, result: ResultCallback<Iterable<Cinema>>) {
        origin.get(location) { result(it.requireNotEmpty()) }
    }

}