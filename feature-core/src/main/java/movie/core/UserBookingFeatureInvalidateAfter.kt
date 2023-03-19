package movie.core

import movie.core.model.Booking
import movie.core.preference.SyncPreference
import movie.core.preference.SyncPreference.Companion.isInThreshold
import java.util.Date
import kotlin.time.Duration

class UserBookingFeatureInvalidateAfter(
    private val origin: UserBookingFeature,
    private val preference: SyncPreference,
    private val duration: Duration
) : UserBookingFeature {

    override suspend fun get(): Result<Sequence<Booking>> {
        val lastRefresh = preference.booking
        if (!lastRefresh.isInThreshold(duration)) {
            return Result.failure(ExpiredException(lastRefresh, duration))
        }
        return origin.get()
    }

    override fun invalidate() {
        preference.booking = Date(0)
    }

}