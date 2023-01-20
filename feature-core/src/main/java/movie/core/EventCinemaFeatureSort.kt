package movie.core

import movie.core.model.Cinema
import movie.core.model.Location

class EventCinemaFeatureSort(
    private val origin: EventCinemaFeature
) : EventCinemaFeature {

    override suspend fun get(location: Location?, result: ResultCallback<Iterable<Cinema>>) {
        origin.get(location) {
            val output = it.map { cinemas ->
                cinemas.sortedWith(compareBy(Cinema::distance, Cinema::city))
            }
            result(output)
        }
    }

}