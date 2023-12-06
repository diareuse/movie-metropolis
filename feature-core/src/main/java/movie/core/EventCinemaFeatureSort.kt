package movie.core

import movie.core.model.Cinema
import movie.core.model.Location

class EventCinemaFeatureSort(
    private val origin: EventCinemaFeature
) : EventCinemaFeature {

    override suspend fun get(location: Location?): Sequence<Cinema> {
        return origin.get(location).sortedWith(compareBy(Cinema::distance, Cinema::city))
    }

}