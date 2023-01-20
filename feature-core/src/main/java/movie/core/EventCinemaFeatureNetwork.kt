package movie.core

import movie.core.adapter.CinemaFromResponse
import movie.core.model.Cinema
import movie.core.model.Location
import movie.core.nwk.CinemaService

class EventCinemaFeatureNetwork(
    private val service: CinemaService
) : EventCinemaFeature {

    override suspend fun get(location: Location?, result: ResultCallback<Iterable<Cinema>>) {
        val data = service.getCinemas().getOrThrow().results.map(::CinemaFromResponse)
        result(Result.success(data))
    }

}