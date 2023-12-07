package movie.core

import movie.core.Recoverable.Companion.foldCatching
import movie.core.model.FieldUpdate
import movie.core.model.User

class UserDataFeatureFold(
    private vararg val options: UserDataFeature
) : UserDataFeature, Recoverable {

    override suspend fun update(data: Iterable<FieldUpdate>) {
        options.foldCatching {
            it.update(data)
        }
    }

    override suspend fun get(): User = options.foldSimple { get() }

}