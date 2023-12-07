package movie.core.pulse

import movie.core.UserBookingFeature
import movie.core.model.Booking
import movie.pulse.Pulse

class PulseSavingBookings(
    private val user: UserBookingFeature
) : Pulse {

    override suspend fun execute(): Result<Sequence<Booking>> {
        return user.runCatching { get() }
    }

}