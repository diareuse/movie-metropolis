package movie.core

import movie.core.model.Booking
import movie.core.preference.SyncPreference
import java.util.Date

class UserBookingFeatureSaveTimestamp(
    private val origin: UserBookingFeature,
    private val preference: SyncPreference
) : UserBookingFeature by origin {

    override suspend fun get(callback: ResultCallback<List<Booking>>) {
        origin.get(callback.then {
            preference.booking = Date()
        })
    }

}