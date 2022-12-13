package movie.metropolis.app.screen.cinema

import movie.core.EventFeature
import movie.core.model.Location
import movie.metropolis.app.model.CinemaView
import movie.metropolis.app.model.adapter.CinemaViewFromFeature

class CinemasFacadeFromFeature(
    private val event: EventFeature
) : CinemasFacade {

    override suspend fun getCinemas(
        latitude: Double?,
        longitude: Double?
    ): Result<List<CinemaView>> = when (latitude == null || longitude == null) {
        true -> event.getCinemas(null)
        else -> event.getCinemas(Location(latitude, longitude))
    }.map {
        it.map(::CinemaViewFromFeature)
    }

}