package movie.core

import movie.core.model.FieldUpdate
import movie.core.model.SignInMethod
import movie.log.logCatchingResult

class UserFeatureRecover(
    private val origin: UserFeature
) : UserFeature by origin {

    override suspend fun signIn(method: SignInMethod) =
        origin.logCatchingResult("user-sign-in") { signIn(method) }

    override suspend fun update(data: Iterable<FieldUpdate>) =
        origin.logCatchingResult("user-update") { update(data) }

    override suspend fun getUser() =
        origin.logCatchingResult("user") { getUser() }

    override suspend fun getBookings() =
        origin.logCatchingResult("user-bookings") { getBookings() }

    override suspend fun getToken() =
        origin.logCatchingResult("user-token") { getToken() }

}