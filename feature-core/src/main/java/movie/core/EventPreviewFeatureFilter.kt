package movie.core

import movie.core.db.dao.BookingDao
import movie.core.model.MoviePreview
import movie.core.preference.EventPreference

class EventPreviewFeatureFilter(
    private val origin: EventPreviewFeature,
    private val preference: EventPreference,
    private val booking: BookingDao
) : EventPreviewFeature {

    override suspend fun get(result: ResultCallback<List<MoviePreview>>) {
        origin.get(result.map outer@{
            if (!preference.filterSeen) {
                return@outer it
            }
            val booking = booking.selectIds()
            it.filter { it.id !in booking }
        })
    }

}