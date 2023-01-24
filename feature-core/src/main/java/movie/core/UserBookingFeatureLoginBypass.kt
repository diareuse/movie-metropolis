package movie.core

import movie.core.model.Booking

class UserBookingFeatureLoginBypass(
    private val origin: UserBookingFeature
) : UserBookingFeature by origin {

    override suspend fun get(callback: ResultCallback<List<Booking>>) {
        origin.get { result ->
            val output = result.recoverCatching {
                when (it) {
                    is SecurityException -> emptyList()
                    else -> throw it
                }
            }
            callback(output)
        }
    }

}