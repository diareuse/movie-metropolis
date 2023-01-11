package movie.core

import movie.core.model.FieldUpdate
import movie.core.model.SignInMethod
import movie.log.flatMapCatching

class UserFeatureRecover(
    private val origin: UserFeature
) : UserFeature by origin {

    override suspend fun signIn(method: SignInMethod) =
        origin.flatMapCatching { signIn(method) }

    override suspend fun update(data: Iterable<FieldUpdate>) =
        origin.flatMapCatching { update(data) }

    override suspend fun getUser() =
        origin.flatMapCatching { getUser() }

    override suspend fun getBookings() =
        origin.flatMapCatching { getBookings() }

    override suspend fun getToken() =
        origin.flatMapCatching { getToken() }

}