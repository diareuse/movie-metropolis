package movie.core

import movie.core.model.FieldUpdate
import movie.core.model.SignInMethod

class UserFeatureRecover(
    private val origin: UserFeature
) : UserFeature {

    override suspend fun signIn(method: SignInMethod) =
        origin.runCatching { signIn(method).getOrThrow() }

    override suspend fun update(data: Iterable<FieldUpdate>) =
        origin.runCatching { update(data).getOrThrow() }

    override suspend fun getUser() =
        origin.runCatching { getUser().getOrThrow() }

    override suspend fun getBookings() =
        origin.runCatching { getBookings().getOrThrow() }

    override suspend fun getToken() =
        origin.runCatching { getToken().getOrThrow() }

}