package movie.metropolis.app.screen.booking

import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import movie.core.ResultCallback
import movie.core.TicketShareRegistry
import movie.core.UserBookingFeature
import movie.core.model.Booking
import movie.metropolis.app.model.BookingView
import movie.metropolis.app.model.adapter.BookingViewActiveFromFeature
import movie.metropolis.app.model.adapter.BookingViewExpiredFromFeature
import movie.metropolis.app.model.facade.Image
import movie.metropolis.app.util.prepare

class BookingFacadeFromFeature(
    private val booking: UserBookingFeature,
    private val share: TicketShareRegistry
) : BookingFacade {

    override suspend fun getBookings(
        callback: ResultCallback<List<BookingView>>
    ) = booking.get { result ->
        callback(result.map { it.map(::BookingViewFromFeature) })
    }

    override suspend fun refresh() {
        booking.invalidate()
    }

    override suspend fun getImage(view: BookingView): Image? {
        if (view !is BookingViewActiveFromFeature) return null
        val shareableText = share.get(view.booking)
        return MultiFormatWriter()
            .prepare(500, 300, BarcodeFormat.PDF_417)
            .getImage(shareableText.decodeToString())
    }

    @Suppress("FunctionName")
    private fun BookingViewFromFeature(booking: Booking) = when (booking) {
        is Booking.Active -> BookingViewActiveFromFeature(booking)
        is Booking.Expired -> BookingViewExpiredFromFeature(booking)
    }

}