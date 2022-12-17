package movie.metropolis.app.screen.cinema

import movie.core.EventFeature
import movie.metropolis.app.model.CinemaView
import movie.metropolis.app.model.Filter
import movie.metropolis.app.model.MovieBookingView
import movie.metropolis.app.model.adapter.CinemaFromView
import movie.metropolis.app.model.adapter.CinemaViewFromFeature
import movie.metropolis.app.model.adapter.MovieBookingViewFromFeature
import movie.metropolis.app.screen.OnChangedListener
import java.util.Date

class CinemaFacadeFromFeature(
    private val id: String,
    private val event: EventFeature
) : CinemaFacade {

    override suspend fun getOptions(): Result<Map<Filter.Type, List<Filter>>> =
        Result.failure(NotImplementedError())

    override fun toggle(filter: Filter) = Unit
    override fun addOnChangedListener(listener: OnChangedListener) = listener
    override fun removeOnChangedListener(listener: OnChangedListener) = Unit

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

