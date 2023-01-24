package movie.core.pulse

import movie.core.UserBookingFeature
import movie.core.model.Booking
import movie.pulse.Pulse

class PulseSavingBookings(
    private val user: UserBookingFeature
) : Pulse {

    override suspend fun execute(): Result<List<Booking>> {
        var result: Result<List<Booking>> = Result.failure(IndexOutOfBoundsException())
        user.get { result = it }
        return result
    }

}