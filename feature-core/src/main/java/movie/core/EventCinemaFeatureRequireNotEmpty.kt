package movie.core

import movie.core.model.Cinema
import movie.core.model.Location

class EventCinemaFeatureRequireNotEmpty(
    private val origin: EventCinemaFeature
) : EventCinemaFeature {

    override suspend fun get(location: Location?): Sequence<Cinema> {
        return origin.get(location).also { require(it.count() > 0) }
    }

}