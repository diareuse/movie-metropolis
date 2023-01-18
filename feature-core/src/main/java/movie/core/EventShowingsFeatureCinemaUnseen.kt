package movie.core

import movie.core.db.dao.BookingDao
import movie.core.preference.EventPreference
import java.util.Date

class EventShowingsFeatureCinemaUnseen(
    private val origin: EventShowingsFeature.Cinema,
    private val preferences: EventPreference,
    private val booking: BookingDao
) : EventShowingsFeature.Cinema {

    @Volatile
    private var seenIds = setOf<String>()

    override suspend fun get(date: Date, result: ResultCallback<MovieWithShowings>) {
        origin.get(date) inner@{
            if (!preferences.filterSeen)
                return@inner result(it)
            val output = it.map { items ->
                val ids = getSeenIds()
                items.filter { (key, _) -> key.id !in ids }
            }
            result(output)
        }
    }

    private suspend fun getSeenIds(): Set<String> {
        if (seenIds.isNotEmpty()) return seenIds
        seenIds = booking.selectIds().toSet()
        return seenIds
    }

}