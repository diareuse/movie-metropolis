package movie.metropolis.app.presentation.cinema

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import movie.cinema.city.CinemaCity
import movie.metropolis.app.model.CinemaView
import movie.metropolis.app.model.Filter
import movie.metropolis.app.model.MovieBookingView
import movie.metropolis.app.model.adapter.CinemaViewFromCinema
import movie.metropolis.app.model.adapter.MovieBookingViewFilter
import movie.metropolis.app.model.adapter.MovieBookingViewFromEvents
import movie.metropolis.app.util.retryOnNetworkError
import java.util.Date

class CinemaFacadeCinemaCity(
    private val id: String,
    private val cinemaCity: CinemaCity
) : CinemaFacade {

    private val filterable = ShowingFilterable()

    override val options get() = filterable.options

    override val cinema: Flow<CinemaView> = flow {
        cinema().let(::CinemaViewFromCinema)
    }

    override fun showings(date: Date): Flow<List<MovieBookingView>> {
        return flow {
            val views = cinemaCity.events.getEvents(cinema(), date)
                .map { MovieBookingViewFromEvents(it.key, it.value) }
                .also {
                    for (events in it)
                        filterable.addFrom(events.availability.keys)
                }
            emit(views)
        }.combine(filterable.languageFilters.combine(filterable.typeFilters) { l, t ->
            l.filter { it.isSelected }.mapTo(mutableSetOf()) { it.value } to
                    t.filter { it.isSelected }.mapTo(mutableSetOf()) { it.value }
        }) { movies, (languages, types) ->
            movies
                .asSequence()
                .map {
                    MovieBookingViewFilter(
                        languages = languages,
                        types = types,
                        origin = it
                    )
                }
                .filterNot { it.availability.isEmpty() }
                .toList()
        }.retryOnNetworkError()
    }

    override fun toggle(filter: Filter) {
        filterable.toggle(filter)
    }

    // ---

    private suspend fun cinema() = cinemaCity.cinemas.getCinemas().first { it.id == id }

}