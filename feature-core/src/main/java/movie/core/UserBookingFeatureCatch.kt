package movie.core

import movie.core.model.Booking

class UserBookingFeatureCatch(
    private val origin: UserBookingFeature
) : UserBookingFeature by origin, Recoverable {

    override suspend fun get(callback: ResultCallback<List<Booking>>) {
        runCatchingResult(callback) {
            origin.get(it)
        }
    }

}