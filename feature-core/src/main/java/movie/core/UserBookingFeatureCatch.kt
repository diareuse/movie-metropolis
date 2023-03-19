package movie.core

class UserBookingFeatureCatch(
    private val origin: UserBookingFeature
) : UserBookingFeature by origin, Recoverable {

    override suspend fun get() = wrap { origin.get() }

}