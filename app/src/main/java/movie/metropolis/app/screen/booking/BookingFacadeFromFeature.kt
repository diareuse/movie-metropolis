package movie.metropolis.app.screen.booking

import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import movie.core.TicketShareRegistry
import movie.core.UserFeature
import movie.core.model.Booking
import movie.metropolis.app.model.BookingView
import movie.metropolis.app.model.adapter.BookingViewActiveFromFeature
import movie.metropolis.app.model.adapter.BookingViewExpiredFromFeature
import movie.metropolis.app.model.facade.Image
import movie.metropolis.app.util.prepare

class BookingFacadeFromFeature(
    private val feature: UserFeature,
    private val online: UserFeature,
    private val share: TicketShareRegistry
) : BookingFacade {

    override suspend fun getBookings() = feature.getBookings()
        .map { it.map(::BookingViewFromFeature) }

    override suspend fun refresh() {
        online.getBookings()
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