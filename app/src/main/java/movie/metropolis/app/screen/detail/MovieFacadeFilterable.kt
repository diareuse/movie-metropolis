package movie.metropolis.app.screen.detail

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import movie.metropolis.app.model.Filter
import movie.metropolis.app.model.adapter.CinemaBookingViewFilter
import movie.metropolis.app.screen.cinema.BookingFilterable.OnChangedListener
import movie.metropolis.app.screen.cinema.Listenable
import java.util.Date

class MovieFacadeFilterable(
    private val origin: MovieFacade
) : MovieFacade by origin {

    private val listenable = Listenable<OnChangedListener>()
    private val selected = mutableSetOf<String>()
    private val options = mutableSetOf<String>()
    private val mutex = Mutex(true)

    override suspend fun getOptions() = mutex.withLock {
        val values = options
            .map { Filter(it in selected, it) }
            .sortedByDescending { it.isSelected }
        Result.success(values)
    }

    override fun toggle(filter: Filter) {
        if (filter.value in selected) selected.remove(filter.value)
        else selected.add(filter.value)
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
        val filters = it.asSequence()
            .flatMap { it.availability.keys }
            .flatMap { listOf(it.language, it.type) }
            .toSet()
        updateFilters(filters)
        if (mutex.isLocked)
            mutex.unlock()
    }.map { cinemas ->
        val options = getOptionKeys() ?: return@map cinemas
        cinemas
            .asSequence()
            .map { CinemaBookingViewFilter(options, it) }
            .filterNot { it.availability.isEmpty() }
            .toList()
    }

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