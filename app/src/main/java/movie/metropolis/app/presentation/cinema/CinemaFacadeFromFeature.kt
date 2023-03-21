package movie.metropolis.app.presentation.cinema

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import movie.core.EventCinemaFeature
import movie.core.EventShowingsFeature
import movie.metropolis.app.model.CinemaView
import movie.metropolis.app.model.MovieBookingView
import movie.metropolis.app.model.adapter.CinemaFromId
import movie.metropolis.app.model.adapter.CinemaViewFromFeature
import movie.metropolis.app.model.adapter.MovieBookingViewFromFeature
import java.util.Date

class CinemaFacadeFromFeature(
    private val id: String,
    private val cinemas: EventCinemaFeature,
    private val showings: EventShowingsFeature.Factory
) : CinemaFacade {

    override val cinema: Flow<Result<CinemaView>> = flow {
        emit(cinemas.get(null).map { it.first { it.id == id }.let(::CinemaViewFromFeature) })
    }

    override fun showings(date: Date): Flow<Result<List<MovieBookingView>>> = flow {
        val cinema = CinemaFromId(id)
        val result = showings.cinema(cinema).get(date)
        val output = result.getOrThrow().entries
            .map { (movie, events) -> MovieBookingViewFromFeature(movie, events) }
            .sortedByDescending { it.availability.entries.sumOf { it.value.size } }
            .let(Result.Companion::success)
        emit(output)
    }

}