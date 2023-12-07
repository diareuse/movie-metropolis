package movie.core

import movie.core.model.Booking

class UserBookingFeatureFold(
    private vararg val options: UserBookingFeature
) : UserBookingFeature, Recoverable {

    override suspend fun get(): Sequence<Booking> = options.foldSimple { get() }

    override fun invalidate() {
        for (option in options)
            option.invalidate()
    }

}