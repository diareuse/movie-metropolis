package movie.metropolis.app.screen.cinema

import movie.log.logCatchingResult

class CinemasFacadeRecover(
    private val origin: CinemasFacade
) : CinemasFacade {

    override suspend fun getCinemas(
        latitude: Double?,
        longitude: Double?
    ) = origin.logCatchingResult("cinemas") { getCinemas(latitude, longitude) }

}