package movie.metropolis.app.feature.global

import java.util.Date
import kotlin.time.Duration

interface UserAccount {

    val isLoggedIn: Boolean
    var token: String?
    var refreshToken: String?
    var expirationDate: Date?

    val isExpired
        get() = Date().after(expirationDate ?: Date(Long.MAX_VALUE))

    fun expiresWithin(threshold: Duration): Boolean {
        val now = Date()
        val expiration = expirationDate ?: return false
        val difference = expiration.time - now.time
        return difference in 500 until threshold.inWholeMilliseconds
    }

    fun delete()

}