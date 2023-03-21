package movie.metropolis.app.presentation.cinema

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onEach
import movie.metropolis.app.model.Filter
import movie.metropolis.app.model.MovieBookingView
import movie.metropolis.app.model.adapter.MovieBookingViewFilter
import movie.metropolis.app.util.mapResult
import java.util.Date

class CinemaFacadeFilterable(
    private val origin: CinemaFacade
) : CinemaFacade.Filterable, CinemaFacade by origin {

    private val filterable = ShowingFilterable()

    override val options = MutableStateFlow(mapOf<Filter.Type, List<Filter>>())

    override fun showings(date: Date): Flow<Result<List<MovieBookingView>>> = options
        .flatMapLatest { origin.showings(date) }
        .onEach { result ->
            result.onSuccess {
                val availability = it.flatMap { it.availability.keys }
                filterable.addFrom(availability)
                updateOptions()
            }
        }.mapResult { movies ->
            movies
                .asSequence()
                .map { MovieBookingViewFilter(filterable, it) }
                .filterNot { it.availability.isEmpty() }
                .toList()
        }

    private fun updateOptions() {
        if (filterable.selectedLanguages.isEmpty())
            filterable.selectAll()
        options.value = buildMap {
            put(Filter.Type.Language, filterable.getLanguages())
            put(Filter.Type.Projection, filterable.getTypes())
        }
    }

    override fun toggle(filter: Filter) {
        filterable.toggle(filter)
        updateOptions()
    }

}

