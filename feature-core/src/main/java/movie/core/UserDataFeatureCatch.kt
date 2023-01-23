package movie.core

import movie.core.model.FieldUpdate
import movie.core.model.User
import movie.log.logSevere

class UserDataFeatureCatch(
    private val origin: UserDataFeature
) : UserDataFeature, Recoverable {

    override suspend fun update(data: Iterable<FieldUpdate>) {
        origin.runCatching { update(data) }.logSevere()
    }

    override suspend fun get(callback: ResultCallback<User>) {
        runCatchingResult(callback) {
            origin.get(it)
        }
    }

}