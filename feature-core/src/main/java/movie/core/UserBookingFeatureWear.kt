package movie.core

import com.google.android.gms.wearable.DataMap
import movie.core.model.Booking
import movie.wear.WearService

/**
 * Syncs following structure at `/bookings` path:
 *
 * ```json
 * {
 *   "bookings": [
 *     {
 *       "id": "string",
 *       "cinema": "string",
 *       "starts_at": 1234567890,
 *       "hall": "string",
 *       "seats": [
 *         {
 *           "row": "string",
 *           "seat": "string"
 *         }
 *       ],
 *       "name": "string"
 *     }
 *   ]
 * }
 * ```
 * */
class UserBookingFeatureWear(
    private val origin: UserBookingFeature,
    private val wear: WearService
) : UserBookingFeature by origin {

    override suspend fun get(callback: ResultCallback<List<Booking>>) {
        origin.get(callback.then {
            val active = it.filterIsInstance<Booking.Active>()
            when {
                active.isEmpty() -> wear.remove("/bookings")
                else -> wear.send("/bookings", active.asDataMap())
            }
        })
    }

    private fun List<Booking.Active>.asDataMap() = DataMap().also { map ->
        map.putDataMapArrayList("bookings", map { it.asDataMap() }.let(::ArrayList))
    }

    private fun Booking.Active.asDataMap() = DataMap().also { map ->
        map.putString("id", id)
        map.putString("cinema", cinema.name)
        map.putLong("starts_at", startsAt.time)
        map.putString("hall", hall)
        map.putDataMapArrayList("seats", seats.asDataMap())
        map.putString("name", movie.name)
    }

    private fun List<Booking.Active.Seat>.asDataMap() = map { it.asDataMap() }
        .let(::ArrayList)

    private fun Booking.Active.Seat.asDataMap() = DataMap().also { map ->
        map.putString("row", row)
        map.putString("seat", seat)
    }

}