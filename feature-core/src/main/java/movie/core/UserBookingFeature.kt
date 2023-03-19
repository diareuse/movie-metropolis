package movie.core

import movie.core.model.Booking

interface UserBookingFeature {
    suspend fun get(): Result<Sequence<Booking>>
    fun invalidate()
}