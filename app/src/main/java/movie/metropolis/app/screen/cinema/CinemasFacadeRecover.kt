package movie.metropolis.app.screen.cinema

class CinemasFacadeRecover(
    private val origin: CinemasFacade
) : CinemasFacade {

    override suspend fun getCinemas(
        latitude: Double?,
        longitude: Double?
    ) = kotlin.runCatching { origin.getCinemas(latitude, longitude).getOrThrow() }

}