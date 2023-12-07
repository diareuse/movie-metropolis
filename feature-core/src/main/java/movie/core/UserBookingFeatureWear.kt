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

    override suspend fun get(): Sequence<Booking> = origin.get().also {
        val active = mutableListOf<Booking>()
        val expired = mutableListOf<Booking>()
        for (booking in it) when (booking.expired) {
            true -> expired += booking
            else -> active += booking
        }
        update("/bookings/active", active.asDataMap())
        update("/bookings/expired", expired.asDataMap())
    }

    // ---

    private suspend fun update(path: String, data: DataMap) {
        if (data.isEmpty) wear.remove(path)
        else wear.send(path, data)
    }

    // ---

    private fun List<Booking>.asDataMap() = DataMap().also { map ->
        if (isNotEmpty())
            map.putDataMapArrayList("bookings", map { it.asDataMap() }.let(::ArrayList))
    }

    private fun Booking.asDataMap() = DataMap().also { map ->
        map.putString("id", id)
        map.putString("cinema", cinema.name)
        map.putLong("starts_at", startsAt.time)
        map.putString("hall", hall)
        map.putDataMapArrayList("seats", seats.asDataMap())
        map.putString("name", name)
    }

    private fun List<Booking.Seat>.asDataMap() = map { it.asDataMap() }
        .let(::ArrayList)

    private fun Booking.Seat.asDataMap() = DataMap().also { map ->
        map.putString("row", row)
        map.putString("seat", seat)
    }

}