package movie.metropolis.app.screen.cinema

import movie.log.flatMapCatching
import movie.log.logSevere

class CinemasFacadeRecover(
    private val origin: CinemasFacade
) : CinemasFacade {

    override suspend fun getCinemas(
        latitude: Double?,
        longitude: Double?
    ) = origin.flatMapCatching { getCinemas(latitude, longitude) }.logSevere()

}