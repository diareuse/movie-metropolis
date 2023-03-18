package movie.metropolis.app.presentation.cinema

import movie.core.EventCinemaFeature
import movie.core.EventShowingsFeature
import movie.core.MutableResult
import movie.core.ResultCallback
import movie.metropolis.app.model.CinemaView
import movie.metropolis.app.model.Filter
import movie.metropolis.app.model.MovieBookingView
import movie.metropolis.app.model.adapter.CinemaFromView
import movie.metropolis.app.model.adapter.CinemaViewFromFeature
import movie.metropolis.app.model.adapter.MovieBookingViewFromFeature
import movie.metropolis.app.presentation.OnChangedListener
import java.util.Date

class CinemaFacadeFromFeature(
    private val id: String,
    private val cinemas: EventCinemaFeature,
    private val showings: EventShowingsFeature.Factory
) : CinemaFacade {

    override suspend fun getOptions(): Result<Map<Filter.Type, List<Filter>>> =
        Result.failure(NotImplementedError())

    override fun toggle(filter: Filter) = Unit
    override fun addOnChangedListener(listener: OnChangedListener) = listener
    override fun removeOnChangedListener(listener: OnChangedListener) = Unit

    override suspend fun getCinema(callback: ResultCallback<CinemaView>) {
        val output = cinemas.get(null).mapCatching { result ->
            result.first { it.id == id }
                .let(::CinemaViewFromFeature)
        }
        callback(output)
    }

    override suspend fun getShowings(date: Date, callback: ResultCallback<List<MovieBookingView>>) {
        val cinema = MutableResult.getOrThrow { getCinema(it.asResultCallback()) }
            .let(::CinemaFromView)
        showings.cinema(cinema).get(date) { result ->
            val output = result.getOrThrow().entries
                .map { (movie, events) -> MovieBookingViewFromFeature(movie, events) }
                .sortedByDescending { it.availability.entries.sumOf { it.value.size } }
                .let(Result.Companion::success)
            callback(output)
        }
    }

}

