package movie.core

import movie.core.model.Cinema
import movie.core.model.Location

class EventCinemaFeatureSort(
    private val origin: EventCinemaFeature
) : EventCinemaFeature {

    override suspend fun get(location: Location?, result: ResultCallback<Iterable<Cinema>>) {
        origin.get(location, result.map {
            it.sortedWith(compareBy(Cinema::distance, Cinema::city))
        })
    }

}