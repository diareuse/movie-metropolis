package movie.core

import movie.core.util.requireNotEmpty

class UserBookingFeatureRequireNotEmpty(
    private val origin: UserBookingFeature
) : UserBookingFeature by origin {

    override suspend fun get() = origin.get().mapCatching { it.requireNotEmpty() }

}