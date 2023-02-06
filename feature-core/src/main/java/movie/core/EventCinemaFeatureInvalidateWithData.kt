package movie.core

import movie.core.model.Cinema
import movie.core.model.Location

class EventCinemaFeatureInvalidateWithData(
    private val origin: EventCinemaFeature
) : EventCinemaFeature {

    override suspend fun get(location: Location?, result: ResultCallback<Iterable<Cinema>>) {
        origin.get(location) { output ->
            val out = output.mapCatching { cinemas -> cinemas.onEach { requireNotNull(it.image) } }
            result(out)
        }
    }

}