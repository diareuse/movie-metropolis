package movie.core

class UserBookingFeatureFold(
    private vararg val options: UserBookingFeature
) : UserBookingFeature, Recoverable {

    override suspend fun get() = options.fold { get() }

    override fun invalidate() {
        for (option in options)
            option.invalidate()
    }

}