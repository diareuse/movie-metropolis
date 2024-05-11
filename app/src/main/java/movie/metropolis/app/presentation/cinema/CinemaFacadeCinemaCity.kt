package movie.metropolis.app.presentation.cinema

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import movie.cinema.city.CinemaCity
import movie.cinema.city.Movie
import movie.cinema.city.Occurrence
import movie.metropolis.app.model.AvailabilityView
import movie.metropolis.app.model.CinemaView
import movie.metropolis.app.model.Filter
import movie.metropolis.app.model.MovieBookingView
import movie.metropolis.app.model.adapter.MovieBookingViewFilter
import movie.metropolis.app.presentation.booking.CinemaViewFromCinema
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
        }
    }

    override fun toggle(filter: Filter) {
        filterable.toggle(filter)
    }

    // ---

    private suspend fun cinema() = cinemaCity.cinemas.getCinemas().first { it.id == id }

}

data class MovieBookingViewFromEvents(
    private val _movie: Movie,
    private val _occurrences: List<Occurrence>
) : MovieBookingView {

    override val movie: MovieBookingView.Movie = MovieImpl()
    override val availability: Map<AvailabilityView.Type, List<AvailabilityView>>
        get() = _occurrences.groupBy(
            keySelector = {
                Type(
                    it.flags.map { it.tag },
                    it.dubbing.toString() + "/" + (it.subtitles?.toString() ?: "-")
                )
            },
            valueTransform = {
                Availability(it)
            }
        )

    private data class Availability(
        private val occurrence: Occurrence
    ) : AvailabilityView {
        override val id: String
            get() = occurrence.id
        override val url: String
            get() = occurrence.booking.toString()
        override val startsAt: String
            get() = occurrence.startsAt.toString()
        override val isEnabled: Boolean
            get() = Date().before(occurrence.startsAt)
    }

    private data class Type(
        override val types: List<String>,
        override val language: String
    ) : AvailabilityView.Type

    private inner class MovieImpl : MovieBookingView.Movie {
        override val id: String
            get() = _movie.id
        override val name: String
            get() = _movie.name.localized
        override val releasedAt: String
            get() = _movie.releasedAt.toString()
        override val duration: String
            get() = _movie.length?.toString().orEmpty()
        override val poster: String
            get() = _movie.images.maxBy { it.height * it.width }.url.toString()
        override val video: String?
            get() = _movie.videos.firstOrNull()?.toString()
    }
}