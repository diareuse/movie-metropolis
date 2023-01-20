package movie.core

import movie.core.Recoverable.Companion.foldCatching
import movie.core.model.Cinema
import movie.core.model.Location

class EventCinemaFeatureFold(
    private vararg val options: EventCinemaFeature
) : EventCinemaFeature, Recoverable {

    override suspend fun get(location: Location?, result: ResultCallback<Iterable<Cinema>>) {
        options.foldCatching { option ->
            option.get(location) { result(it.onFailureThrow()) }
        }
    }

}