package movie.core

import movie.core.model.Cinema
import movie.core.model.Location

class EventCinemaFeatureFold(
    private vararg val options: EventCinemaFeature
) : EventCinemaFeature, Recoverable {

    override suspend fun get(location: Location?): Sequence<Cinema> {
        return options.foldSimple { get(location) }
    }

}