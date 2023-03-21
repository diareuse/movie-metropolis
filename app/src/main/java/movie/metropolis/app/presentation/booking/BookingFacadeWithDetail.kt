package movie.metropolis.app.presentation.booking

import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import movie.core.EventDetailFeature
import movie.core.adapter.MovieFromId
import movie.core.model.MovieDetail
import movie.log.logSevere
import movie.metropolis.app.model.BookingView
import movie.metropolis.app.model.adapter.BookingViewActiveWithDetail
import movie.metropolis.app.model.adapter.BookingViewExpiredWithDetail
import movie.metropolis.app.util.flatMapResult

class BookingFacadeWithDetail(
    private val origin: BookingFacade,
    private val detail: EventDetailFeature
) : BookingFacade by origin {

    override val bookings = origin.bookings.flatMapResult { movies ->
        withDetail(movies).map(Result.Companion::success)
    }

    private fun withDetail(movies: List<BookingView>) = channelFlow {
        val output = movies.toMutableList()
        send(output)
        val locks = mutableMapOf<String, Mutex>()
        for ((index, booking) in output.withIndex()) launch {
            val movie = booking.movie
            locks.getOrPut(movie.id) { Mutex() }.withLock {
                val detail =
                    detail.get(MovieFromId(movie.id)).logSevere().getOrNull() ?: return@launch
                val view = BookingViewWithDetail(booking, detail)
                output[index] = view
                send(output.toList())
            }
        }
    }

    @Suppress("FunctionName")
    private fun BookingViewWithDetail(view: BookingView, detail: MovieDetail) = when (view) {
        is BookingView.Active -> BookingViewActiveWithDetail(view, detail)
        is BookingView.Expired -> BookingViewExpiredWithDetail(view, detail)
        else -> view
    }

}