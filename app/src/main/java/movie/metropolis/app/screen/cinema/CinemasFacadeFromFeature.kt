package movie.metropolis.app.screen.cinema

import movie.metropolis.app.feature.global.EventFeature
import movie.metropolis.app.feature.global.Location
import movie.metropolis.app.model.CinemaView
import movie.metropolis.app.model.CinemaViewFromFeature

class CinemasFacadeFromFeature(
    private val event: EventFeature
) : CinemasFacade {

    override suspend fun getCinemas(
        latitude: Double?,
        longitude: Double?
    ): Result<List<CinemaView>> {
        if (latitude == null || longitude == null)
            return event.getCinemas(null).map { it.map(::CinemaViewFromFeature) }

        val location = Location(latitude, longitude)
        return event.getCinemas(location).map { it.map(::CinemaViewFromFeature) }
    }

}