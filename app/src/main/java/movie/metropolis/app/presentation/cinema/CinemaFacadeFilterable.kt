package movie.metropolis.app.presentation.cinema

import kotlinx.coroutines.sync.Mutex
import movie.core.ResultCallback
import movie.metropolis.app.model.Filter
import movie.metropolis.app.model.MovieBookingView
import movie.metropolis.app.model.adapter.MovieBookingViewFilter
import movie.metropolis.app.presentation.Listenable
import movie.metropolis.app.presentation.OnChangedListener
import java.util.Date

class CinemaFacadeFilterable(
    private val origin: CinemaFacade
) : CinemaFacade by origin {

    private val filterable = ShowingFilterable()
    private val listenable = Listenable<OnChangedListener>()
    private val mutex = Mutex(true)

    override suspend fun getOptions(): Result<Map<Filter.Type, List<Filter>>> {
        if (mutex.isLocked) mutex.lock()
        val output = buildMap {
            put(Filter.Type.Language, filterable.getLanguages())
            put(Filter.Type.Projection, filterable.getTypes())
        }
        val count = output.toList().sumOf { (_, value) -> value.size }
        return when {
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

    override suspend fun getShowings(date: Date, callback: ResultCallback<List<MovieBookingView>>) {
        origin.getShowings(date) { result ->
            val output = result.onSuccess {
                val availableTypes = it.asSequence().flatMap { it.availability.keys }
                if (filterable.addFrom(availableTypes.asIterable())) {
                    filterable.selectAll()
                }
                if (mutex.isLocked) {
                    mutex.unlock()
                }
            }.map { movies ->
                movies
                    .asSequence()
                    .map { MovieBookingViewFilter(filterable, it) }
                    .filterNot { it.availability.isEmpty() }
                    .toList()
            }
            callback(output)
        }
    }

}

