package movie.metropolis.app.screen.cinema

import movie.core.EventFeature
import movie.metropolis.app.model.CinemaView
import movie.metropolis.app.model.MovieBookingView
import movie.metropolis.app.model.adapter.CinemaFromView
import movie.metropolis.app.model.adapter.CinemaViewFromFeature
import movie.metropolis.app.model.adapter.MovieBookingViewFromFeature
import java.util.Date

class CinemaFacadeFromFeature(
    private val id: String,
    private val event: EventFeature
) : CinemaFacade {

    override suspend fun getCinema(): Result<CinemaView> {
        return event.getCinemas(null)
            .mapCatching { cinemas -> cinemas.first { it.id == id } }
            .map(::CinemaViewFromFeature)
    }

    override suspend fun getShowings(date: Date): Result<List<MovieBookingView>> {
        val cinema = getCinema()
            .getOrThrow()
            .let(::CinemaFromView)

        val result = event.getShowings(cinema, date)
            .getOrThrow()

        return result.entries
            .map { (movie, events) -> MovieBookingViewFromFeature(movie, events) }
            .sortedByDescending { it.availability.entries.sumOf { it.value.size } }
            .let(Result.Companion::success)
    }

}