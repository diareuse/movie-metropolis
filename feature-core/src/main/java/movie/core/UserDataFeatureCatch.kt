package movie.core

import movie.core.model.FieldUpdate
import movie.log.logSevere

class UserDataFeatureCatch(
    private val origin: UserDataFeature
) : UserDataFeature, Recoverable {

    override suspend fun update(data: Iterable<FieldUpdate>) {
        origin.runCatching { update(data) }.logSevere()
    }

    override suspend fun get() = wrap { origin.get() }

}