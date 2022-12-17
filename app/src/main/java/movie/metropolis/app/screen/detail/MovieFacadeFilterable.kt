package movie.metropolis.app.screen.detail

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import movie.metropolis.app.model.Filter
import movie.metropolis.app.model.adapter.CinemaBookingViewFilter
import movie.metropolis.app.screen.OnChangedListener
import movie.metropolis.app.screen.cinema.Listenable
import movie.metropolis.app.screen.cinema.ShowingFilterable
import java.util.Date

class MovieFacadeFilterable(
    private val origin: MovieFacade
) : MovieFacade by origin {

    private val filterable = ShowingFilterable()
    private val listenable = Listenable<OnChangedListener>()
    private val mutex = Mutex(true)

    override suspend fun getOptions() = mutex.withLock {
        val output = buildMap {
            put(Filter.Type.Language, filterable.getLanguages())
            put(Filter.Type.Projection, filterable.getTypes())
        }
        Result.success(output)
    }

    override fun toggle(filter: Filter) {
        filterable.toggle(filter)
        listenable.notify { onChanged() }
    }

    override fun addOnChangedListener(listener: OnChangedListener) = listener.apply {
        listenable += this
    }

    override fun removeOnChangedListener(listener: OnChangedListener) {
        listenable -= listener
    }

    override suspend fun getShowings(
        date: Date,
        latitude: Double,
        longitude: Double
    ) = origin.getShowings(date, latitude, longitude).onSuccess {
        val availableTypes = it.asSequence().flatMap { it.availability.keys }
        filterable.addFrom(availableTypes.asIterable())
        listenable.notify { onChanged() }
        if (mutex.isLocked) {
            filterable.selectAll()
            mutex.unlock()
        }
    }.map { cinemas ->
        cinemas
            .asSequence()
            .map { CinemaBookingViewFilter(filterable.getSelectedTags(), it) }
            .filterNot { it.availability.isEmpty() }
            .toList()
    }

}