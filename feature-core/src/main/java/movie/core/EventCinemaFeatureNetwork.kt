package movie.core

import movie.core.adapter.CinemaFromResponse
import movie.core.model.Cinema
import movie.core.model.Location
import movie.core.nwk.CinemaService
import movie.core.nwk.EndpointProvider

class EventCinemaFeatureNetwork(
    private val service: CinemaService,
    private val provider: EndpointProvider
) : EventCinemaFeature {

    override suspend fun get(location: Location?): Sequence<Cinema> {
        return service.getCinemas().getOrThrow()
            .results.asSequence()
            .map { CinemaFromResponse(it, null, provider) }
    }

}