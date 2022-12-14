package movie.metropolis.app.screen.cinema

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import movie.metropolis.app.model.Filter
import movie.metropolis.app.model.adapter.MovieBookingViewFilter
import movie.metropolis.app.screen.OnChangedListener
import java.util.Date

class CinemaFacadeFilterable(
    private val origin: CinemaFacade
) : CinemaFacade by origin {

    private val filterable = ShowingFilterable()
    private val listenable = Listenable<OnChangedListener>()
    private val mutex = Mutex(true)

    override suspend fun getOptions() = mutex.withLock {
        val output = buildMap {
            put(Filter.Type.Language, filterable.getLanguages())
            put(Filter.Type.Projection, filterable.getTypes())
        }
        val count = output.toList().sumOf { (_, value) -> value.size }
        when {
            count <= 0 -> Result.failure(IndexOutOfBoundsException())
            else -> Result.success(output)
        }
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

    override suspend fun getShowings(date: Date) = origin.getShowings(date).onSuccess {
        val availableTypes = it.asSequence().flatMap { it.availability.keys }
        filterable.addFrom(availableTypes.asIterable())
        listenable.notify { onChanged() }
        if (mutex.isLocked) {
            filterable.selectAll()
            mutex.unlock()
        }
    }.map { movies ->
        movies
            .asSequence()
            .map { MovieBookingViewFilter(filterable, it) }
            .filterNot { it.availability.isEmpty() }
            .toList()
    }

}

