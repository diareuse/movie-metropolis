package movie.metropolis.app.presentation.cinema

import movie.core.EventCinemaFeature
import movie.core.ResultCallback
import movie.core.model.Location
import movie.metropolis.app.model.CinemaView
import movie.metropolis.app.model.adapter.CinemaViewFromFeature

class CinemasFacadeFromFeature(
    private val cinemas: EventCinemaFeature
) : CinemasFacade {

    override suspend fun getCinemas(
        latitude: Double?,
        longitude: Double?,
        callback: ResultCallback<List<CinemaView>>
    ) {
        val location = when (latitude == null || longitude == null) {
            true -> null
            else -> Location(latitude, longitude)
        }
        val output = cinemas.get(location).map { result ->
            result.map(::CinemaViewFromFeature).toList()
        }
        callback(output)
    }

}