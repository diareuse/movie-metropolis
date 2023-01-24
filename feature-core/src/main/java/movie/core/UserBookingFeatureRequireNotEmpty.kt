package movie.core

import movie.core.model.Booking
import movie.core.util.requireNotEmpty

class UserBookingFeatureRequireNotEmpty(
    private val origin: UserBookingFeature
) : UserBookingFeature by origin {

    override suspend fun get(callback: ResultCallback<List<Booking>>) {
        origin.get(callback.map {
            it.requireNotEmpty()
        })
    }

}