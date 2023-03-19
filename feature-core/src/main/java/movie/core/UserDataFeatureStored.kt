package movie.core

import movie.core.adapter.UserFromStored
import movie.core.model.FieldUpdate
import movie.core.preference.UserPreference

class UserDataFeatureStored(
    private val preference: UserPreference,
    private val cinema: EventCinemaFeature
) : UserDataFeature {

    override suspend fun update(data: Iterable<FieldUpdate>) {
        throw IllegalStateException("Not supported by offline storage")
    }

    override suspend fun get() = cinema.get(null).mapCatching {
        UserFromStored(preference, it.asIterable())
    }

}