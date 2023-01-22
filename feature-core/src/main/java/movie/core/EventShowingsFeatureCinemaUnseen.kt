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
        origin.get(date, result.map inner@{
            if (!preferences.filterSeen)
                return@inner it
            val ids = getSeenIds()
            it.filter { (key, _) -> key.id !in ids }
        })
    }

    private suspend fun getSeenIds(): Set<String> {
        if (seenIds.isNotEmpty()) return seenIds
        seenIds = booking.selectIds().toSet()
        return seenIds
    }

}