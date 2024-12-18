package movie.metropolis.app.feature.cinemacity

import movie.cinema.city.TokenStore
import movie.core.auth.UserAccount

class TokenStoreImpl(
    private val user: UserAccount
) : TokenStore {
    override var token: String
        get() = user.token
        set(value) {
            user.token = value
        }
    override var refreshToken: String
        get() = user.refreshToken
        set(value) {
            user.refreshToken = value
        }
}