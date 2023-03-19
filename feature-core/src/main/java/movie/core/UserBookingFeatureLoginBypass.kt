package movie.core

class UserBookingFeatureLoginBypass(
    private val origin: UserBookingFeature
) : UserBookingFeature by origin {

    override suspend fun get() = origin.get().recoverCatching {
        if (it is SecurityException) emptySequence()
        else throw it
    }

}