package movie.core

import movie.core.model.FieldUpdate
import movie.core.model.SignInMethod

class UserFeatureRecoverSecondary(
    private val primary: UserFeature,
    private val secondary: UserFeature
) : UserFeature {

    override val email: String?
        get() = primary.email ?: secondary.email

    override suspend fun signIn(method: SignInMethod) = tryOrRecover {
        signIn(method)
    }

    override suspend fun update(data: Iterable<FieldUpdate>) = tryOrRecover {
        update(data)
    }

    override suspend fun getUser() = tryOrRecover {
        getUser()
    }

    override suspend fun getBookings() = tryOrRecover {
        getBookings()
    }

    override suspend fun getToken() = tryOrRecover {
        getToken()
    }

    // ---

    private inline fun <T> tryOrRecover(body: UserFeature.() -> Result<T>): Result<T> {
        return primary.run(body).recoverCatching { secondary.run(body).getOrThrow() }
    }

}