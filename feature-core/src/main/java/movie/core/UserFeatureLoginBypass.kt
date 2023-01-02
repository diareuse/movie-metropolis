package movie.core

import movie.core.model.Booking

class UserFeatureLoginBypass(
    private val origin: UserFeature
) : UserFeature by origin {

    override suspend fun getBookings(): Result<Iterable<Booking>> {
        val result = origin.getBookings()
        return when (result.exceptionOrNull()) {
            is SecurityException -> Result.success(emptyList())
            else -> result
        }
    }

}