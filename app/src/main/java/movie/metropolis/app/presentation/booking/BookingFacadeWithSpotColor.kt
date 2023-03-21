package movie.metropolis.app.presentation.booking

import androidx.compose.ui.graphics.*
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import movie.image.ImageAnalyzer
import movie.metropolis.app.model.BookingView
import movie.metropolis.app.model.ImageView
import movie.metropolis.app.model.adapter.BookingViewActiveWithPoster
import movie.metropolis.app.model.adapter.BookingViewExpiredWithPoster
import movie.metropolis.app.model.adapter.ImageViewWithColor
import movie.metropolis.app.util.flatMapResult

class BookingFacadeWithSpotColor(
    private val origin: BookingFacade,
    private val analyzer: ImageAnalyzer
) : BookingFacade by origin {

    override val bookings = origin.bookings.flatMapResult {
        withSpotColor(it).map(Result.Companion::success)
    }

    private fun withSpotColor(movies: List<BookingView>) = channelFlow {
        val output = movies.toMutableList()
        send(output)
        val locks = mutableMapOf<String, Mutex>()
        for ((index, booking) in output.withIndex()) launch {
            val poster = booking.movie.poster ?: return@launch
            locks.getOrPut(booking.movie.id) { Mutex() }.withLock {
                val color = analyzer.getColors(poster.url).vibrant.rgb
                val colored = ImageViewWithColor(poster, Color(color))
                output[index] = BookingViewWithPoster(booking, colored)
                send(output.toList())
            }
        }
    }

    @Suppress("FunctionName")
    private fun BookingViewWithPoster(view: BookingView, poster: ImageView) = when (view) {
        is BookingView.Active -> BookingViewActiveWithPoster(view, poster)
        is BookingView.Expired -> BookingViewExpiredWithPoster(view, poster)
        else -> view
    }

}