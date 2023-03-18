package movie.core

import movie.core.model.Cinema
import movie.core.model.Location

class EventCinemaFeatureFold(
    private vararg val options: EventCinemaFeature
) : EventCinemaFeature, Recoverable {

    override suspend fun get(location: Location?): Result<Sequence<Cinema>> {
        return options.fold { get(location) }
    }

}