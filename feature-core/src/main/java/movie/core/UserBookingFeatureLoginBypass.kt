package movie.core

import movie.core.model.Booking

class UserBookingFeatureLoginBypass(
    private val origin: UserBookingFeature
) : UserBookingFeature by origin {

    override suspend fun get(): Sequence<Booking> = try {
        origin.get()
    } catch (ignore: SecurityException) {
        emptySequence()
    }

}