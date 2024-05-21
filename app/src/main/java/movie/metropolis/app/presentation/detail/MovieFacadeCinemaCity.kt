package movie.metropolis.app.presentation.detail

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import movie.cinema.city.CinemaCity
import movie.cinema.city.parallelMap
import movie.metropolis.app.model.CinemaBookingView
import movie.metropolis.app.model.Filter
import movie.metropolis.app.model.MovieDetailView
import movie.metropolis.app.model.adapter.CinemaBookingViewFilter
import movie.metropolis.app.model.adapter.CinemaBookingViewFromCinema
import movie.metropolis.app.model.adapter.MovieDetailViewFromMovie
import movie.metropolis.app.presentation.cinema.ShowingFilterable
import movie.metropolis.app.util.retryOnNetworkError
import java.util.Date

class MovieFacadeCinemaCity(
    private val id: String,
    private val cinemaCity: CinemaCity
) : MovieFacade {

    private val filterable = ShowingFilterable()

    override val movie: Flow<MovieDetailView> = flow {
        emit(cinemaCity.events.getEvent(id).let(::MovieDetailViewFromMovie))
    }.retryOnNetworkError()
    override val availability: Flow<Date> = flowOf(Date())
    override val options get() = filterable.options

    override fun showings(
        date: Date,
        latitude: Double,
        longitude: Double
    ): Flow<List<CinemaBookingView>> = flow {
        val events = cinemaCity.cinemas.getCinemas().parallelMap { cinema ->
            val events = cinemaCity.events.getEvents(cinema, date)
                .entries.find { it.key.id == id }?.value.orEmpty()
            CinemaBookingViewFromCinema(cinema, events)
        }
        for (view in events)
            filterable.addFrom(view.availability.keys)
        emit(events.filter { it.availability.isNotEmpty() })
    }.combine(filterable.languageFilters.combine(filterable.typeFilters) { l, t ->
        l.filter { it.isSelected }.mapTo(mutableSetOf()) { it.value } to
                t.filter { it.isSelected }.mapTo(mutableSetOf()) { it.value }
    }) { movies, (languages, types) ->
        movies
            .asSequence()
            .map {
                CinemaBookingViewFilter(
                    languages = languages,
                    types = types,
                    origin = it
                )
            }
            .filterNot { it.availability.isEmpty() }
            .toList()
    }.retryOnNetworkError()

    override fun toggle(filter: Filter) {
        filterable.toggle(filter)
    }

}