package movie.core

import movie.core.model.Booking

class UserFeatureLoginBypass(
    private val origin: UserFeature
) : UserFeature by origin {

    override suspend fun getBookings(): Result<Iterable<Booking>> {
        return origin.runCatching { getBookings().getOrDefault(emptyList()) }
    }

}