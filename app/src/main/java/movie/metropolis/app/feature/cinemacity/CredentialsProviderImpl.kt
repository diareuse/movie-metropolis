package movie.metropolis.app.feature.cinemacity

import movie.cinema.city.Credentials
import movie.cinema.city.CredentialsProvider
import movie.core.auth.UserAccount

class CredentialsProviderImpl(
    private val user: UserAccount
) : CredentialsProvider {
    override suspend fun get() = Credentials(
        username = user.email.let(::requireNotNull),
        password = user.password.let(::requireNotNull)
    )
}