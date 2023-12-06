package movie.core

import movie.core.model.Cinema
import movie.core.model.Location

class EventCinemaFeatureCatch(
    private val origin: EventCinemaFeature
) : EventCinemaFeature, Recoverable {

    override suspend fun get(location: Location?): Sequence<Cinema> {
        return origin.get(location)
    }

}