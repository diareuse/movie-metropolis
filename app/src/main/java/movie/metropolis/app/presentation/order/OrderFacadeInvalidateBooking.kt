package movie.metropolis.app.presentation.order

import movie.metropolis.app.presentation.booking.BookingFacade

class OrderFacadeInvalidateBooking(
    private val origin: OrderFacade,
    private val booking: BookingFacade
) : OrderFacade by origin {

    override fun setUrl(url: String) {
        origin.setUrl(url)
        if (isCompleted)
            booking.refresh()
    }

}