package movie.metropolis.app.presentation.detail

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onEach
import movie.metropolis.app.model.CinemaBookingView
import movie.metropolis.app.model.Filter
import movie.metropolis.app.model.adapter.CinemaBookingViewFilter
import movie.metropolis.app.presentation.cinema.ShowingFilterable
import movie.metropolis.app.util.mapResult
import java.util.Date

class MovieFacadeFilterable(
    private val origin: MovieFacade
) : MovieFacade.Filterable, MovieFacade by origin {

    private val filterable = ShowingFilterable()

    override val options = MutableStateFlow(mapOf<Filter.Type, List<Filter>>())

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun showings(
        date: Date,
        latitude: Double,
        longitude: Double
    ): Flow<Result<List<CinemaBookingView>>> = options
        .flatMapLatest { origin.showings(date, latitude, longitude) }
        .onEach { result ->
            result.onSuccess {
                val availability = it.flatMap { it.availability.keys }
                filterable.addFrom(availability)
                updateOptions()
            }
        }
        .mapResult { cinemas ->
            cinemas
                .asSequence()
                .map { CinemaBookingViewFilter(filterable, it) }
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