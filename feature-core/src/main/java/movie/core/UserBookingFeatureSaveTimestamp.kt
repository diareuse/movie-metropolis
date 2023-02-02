package movie.core

import kotlinx.coroutines.coroutineScope
import movie.core.model.Booking
import movie.core.preference.SyncPreference
import java.util.Date

class UserBookingFeatureSaveTimestamp(
    private val origin: UserBookingFeature,
    private val preference: SyncPreference
) : UserBookingFeature by origin {

    override suspend fun get(callback: ResultCallback<List<Booking>>) = coroutineScope {
        origin.get(callback.then(this) {
            preference.booking = Date()
        })
    }

}