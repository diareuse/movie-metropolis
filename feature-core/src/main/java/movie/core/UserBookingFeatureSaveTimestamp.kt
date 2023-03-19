package movie.core

import movie.core.preference.SyncPreference
import java.util.Date

class UserBookingFeatureSaveTimestamp(
    private val origin: UserBookingFeature,
    private val preference: SyncPreference
) : UserBookingFeature by origin {

    override suspend fun get() = origin.get().onSuccess {
        preference.booking = Date()
    }

}