package movie.metropolis.app.feature.user

import android.accounts.Account
import android.accounts.AccountManager
import android.content.Context
import android.os.Bundle
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import java.util.Date
import kotlin.time.Duration

interface UserCredentials {
    var email: String?
    var password: String?
}

internal class UserCredentialsImpl(
    context: Context
) : UserCredentials {

    private val key = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .setUserAuthenticationRequired(false)
        .build()

    private val preferences = EncryptedSharedPreferences.create(
        context,
        context.packageName + ".credentials",
        key,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    override var email
        get() = preferences.getString("email", null)
        set(value) = preferences.edit {
            putString("email", value)
        }

    override var password
        get() = preferences.getString("password", null)
        set(value) = preferences.edit {
            putString("password", value)
        }

}

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

}

internal class UserAccountImpl(
    private val manager: AccountManager,
    private val credentials: UserCredentials
) : UserAccount {

    private val account
        get() = credentials.email
            ?.let { Account(it, Type) }
            ?.also(::requireExistence)

    override val isLoggedIn
        get() = account != null

    override var token
        get() = account?.let(manager::getPassword)
        set(value) {
            val account = account ?: return
            manager.setPassword(account, value)
        }

    override var refreshToken
        get() = account?.let { manager.getUserData(it, "refresh-token") }
        set(value) {
            val account = account ?: return
            manager.setUserData(account, "refresh-token", value)
        }

    override var expirationDate: Date?
        get() = account?.let { manager.getUserData(it, "token-expiration") }
            ?.toLongOrNull()
            ?.let(::Date)
        set(value) {
            val account = account ?: return
            manager.setUserData(account, "token-expiration", value?.time?.toString())
        }

    private fun requireExistence(account: Account) {
        if (account in manager.getAccountsByType(Type))
            return
        manager.addAccountExplicitly(account, null, Bundle.EMPTY)
    }

    companion object {

        private const val Type = "movie.metropolis"

    }

}