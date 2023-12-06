package movie.metropolis.app.presentation.booking

import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import movie.core.EventDetailFeature
import movie.core.adapter.MovieFromId
import movie.core.model.MovieDetail
import movie.metropolis.app.model.BookingView
import movie.metropolis.app.model.adapter.BookingViewWithDetail

class BookingFacadeWithDetail(
    private val origin: BookingFacade,
    private val detail: EventDetailFeature
) : BookingFacade by origin {

    private val movieCache = mutableMapOf<String, MovieDetail>()
    private val movieCacheLock = Mutex()
    override val bookings = origin.bookings.flatMapLatest { movies ->
        withDetail(movies)
    }

    private fun withDetail(movies: List<BookingView>) = channelFlow {
        if (movieCache.isEmpty()) send(movies)
        val output = movies.toMutableList()
        val outputLock = Mutex()
        for ((index, booking) in output.withIndex()) launch {
            val movie = booking.movie
            val view = BookingViewWithDetail(booking, getDetail(movie.id) ?: return@launch)
            val o = outputLock.withLock {
                output[index] = view
                output.toList()
            }
            send(o)
        }
    }

    private suspend fun getDetail(id: String): MovieDetail? = movieCacheLock.withLock {
        return movieCache.getOrPut(id) {
            detail.runCatching { get(MovieFromId(id)) }.getOrNull() ?: return@getDetail null
        }
    }

}