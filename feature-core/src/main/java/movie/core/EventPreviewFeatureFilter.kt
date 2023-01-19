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
        origin.get outer@{
            if (!preference.filterSeen) {
                return@outer result(it)
            }
            val booking = booking.selectIds()
            val output = it.getOrThrow().filter { it.id !in booking }
            result(Result.success(output))
        }
    }

}