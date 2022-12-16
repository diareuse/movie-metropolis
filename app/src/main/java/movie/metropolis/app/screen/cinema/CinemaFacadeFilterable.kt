package movie.metropolis.app.screen.cinema

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import movie.metropolis.app.model.Filter
import movie.metropolis.app.model.adapter.MovieBookingViewFilter
import movie.metropolis.app.screen.cinema.BookingFilterable.OnChangedListener
import java.util.Date

class CinemaFacadeFilterable(
    private val origin: CinemaFacade
) : CinemaFacade by origin {

    private val listenable = Listenable<OnChangedListener>()
    private val selected = mutableSetOf<String>()
    private val options = mutableSetOf<String>()
    private val mutex = Mutex(true)

    override suspend fun getOptions(): Result<List<Filter>> = mutex.withLock {
        val output = options
            .map { Filter(it in selected, it) }
            .sortedBy { it.isSelected }
        Result.success(output)
    }

    override fun toggle(filter: Filter) {
        if (filter.value in selected) {
            selected.remove(filter.value)
        } else {
            selected.add(filter.value)
        }
        listenable.notify { onChanged() }
    }

    override fun addOnChangedListener(listener: OnChangedListener) = listener.apply {
        listenable += this
    }

    override fun removeOnChangedListener(listener: OnChangedListener) {
        listenable -= listener
    }

    override suspend fun getShowings(date: Date) = origin.getShowings(date).onSuccess {
        val filters = it.asSequence()
            .flatMap { it.availability.keys }
            .flatMap { listOf(it.language, it.type) }
            .toSet()
        updateFilters(filters)
        if (mutex.isLocked)
            mutex.unlock()
    }.map { movies ->
        val options = getOptionKeys() ?: return@map movies
        movies
            .asSequence()
            .map { MovieBookingViewFilter(options, it) }
            .filterNot { it.availability.isEmpty() }
            .toList()
    }

    // ---

    private fun updateFilters(filters: Set<String>) {
        if (mutex.isLocked) {
            selected.addAll(filters)
        }
        options.addAll(filters)
    }

    private suspend fun getOptionKeys() = getOptions().getOrNull()
        ?.asSequence()
        ?.filter { it.isSelected }
        ?.map { it.value }
        ?.toSet()

}

