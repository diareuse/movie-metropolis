package movie.core

import movie.core.model.Booking

interface UserBookingFeature {
    suspend fun get(callback: ResultCallback<List<Booking>>)
}