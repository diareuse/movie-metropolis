package movie.metropolis.app.presentation.cinema

import android.location.Location
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import movie.cinema.city.CinemaCity
import movie.metropolis.app.model.CinemaView
import movie.metropolis.app.model.adapter.CinemaViewFromCinema
import movie.metropolis.app.util.retryOnNetworkError

class CinemasFacadeCinemaCity(
    private val cinemaCity: CinemaCity
) : CinemasFacade {

    override fun cinemas(location: Location?): Flow<List<CinemaView>> {
        return flow {
            emit(cinemaCity.cinemas.getCinemas().map(::CinemaViewFromCinema))
        }.retryOnNetworkError()
    }

}