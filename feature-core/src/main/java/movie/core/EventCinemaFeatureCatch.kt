package movie.core

import movie.core.model.Cinema
import movie.core.model.Location

class EventCinemaFeatureCatch(
    private val origin: EventCinemaFeature
) : EventCinemaFeature, Recoverable {

    override suspend fun get(location: Location?, result: ResultCallback<Iterable<Cinema>>) {
        runCatchingResult(result) {
            origin.get(location, it)
        }
    }

}