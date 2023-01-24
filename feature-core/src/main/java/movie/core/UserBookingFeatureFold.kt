package movie.core

import movie.core.Recoverable.Companion.foldCatching
import movie.core.model.Booking

class UserBookingFeatureFold(
    private vararg val options: UserBookingFeature
) : UserBookingFeature, Recoverable {

    override suspend fun get(callback: ResultCallback<List<Booking>>) {
        options.foldCatching { option ->
            option.get { callback(it.onFailureThrow()) }
        }
    }

    override fun invalidate() {
        for (option in options)
            option.invalidate()
    }

}