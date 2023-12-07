package movie.core

import movie.core.model.Booking
import movie.core.util.requireNotEmpty

class UserBookingFeatureRequireNotEmpty(
    private val origin: UserBookingFeature
) : UserBookingFeature by origin {

    override suspend fun get(): Sequence<Booking> = origin.get().also { it.requireNotEmpty() }

}