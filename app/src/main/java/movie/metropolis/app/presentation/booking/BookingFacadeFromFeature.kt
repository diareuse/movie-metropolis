package movie.metropolis.app.presentation.booking

import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import movie.core.TicketShareRegistry
import movie.core.UserBookingFeature
import movie.metropolis.app.model.BookingView
import movie.metropolis.app.model.adapter.BookingViewFromFeature
import movie.metropolis.app.model.facade.Image
import movie.metropolis.app.util.prepare

class BookingFacadeFromFeature(
    private val booking: UserBookingFeature,
    private val share: TicketShareRegistry
) : BookingFacade {

    override val bookings: Flow<List<BookingView>> = flow {
        val items = booking.get()
            .map(::BookingViewFromFeature)
            .toList()
        emit(items)
    }

    override fun refresh() {
        booking.invalidate()
    }

    override suspend fun getShareImage(view: BookingView): Image {
        val view = view.origin()
        check(view is BookingViewFromFeature)
        val shareableText = share.get(view.booking)
        return MultiFormatWriter()
            .prepare(500, 300, BarcodeFormat.PDF_417)
            .getImage(shareableText.decodeToString())
    }

}

