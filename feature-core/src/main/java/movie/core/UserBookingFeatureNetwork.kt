package movie.core

import movie.core.adapter.BookingFromResponse
import movie.core.model.Booking
import movie.core.model.Cinema
import movie.core.nwk.UserService
import movie.core.nwk.model.BookingResponse

class UserBookingFeatureNetwork(
    private val service: UserService,
    private val cinema: EventCinemaFeature
) : UserBookingFeature {

    override suspend fun get(): Sequence<Booking> {
        val bookings = service.getBookings().getOrThrow()
        val cinemas = cinema.get(null).asIterable()
        return bookings.map { it.asBooking(cinemas) }.asSequence()
    }

    override fun invalidate() = Unit

    // ---

    private suspend fun BookingResponse.asBooking(cinemas: Iterable<Cinema>) = when (isExpired()) {
        //true -> BookingExpiredFromResponse(this, cinemas)
        else -> BookingFromResponse(this, getDetail(this), cinemas)
    }

    private suspend fun getDetail(booking: BookingResponse) =
        service.getBooking(booking.id).getOrThrow()

}