package movie.core

import movie.core.db.dao.BookingDao
import movie.core.preference.EventPreference

class EventPreviewFeatureFilter(
    private val origin: EventPreviewFeature,
    private val preference: EventPreference,
    private val booking: BookingDao
) : EventPreviewFeature {

    override suspend fun get() = origin.get().map { items ->
        if (!preference.filterSeen) {
            return@map items
        }
        val booking = booking.selectIds()
        items.filter { it.id !in booking }
    }

}