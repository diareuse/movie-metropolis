package movie.core.auth

import android.accounts.Account
import android.accounts.AccountManager
import android.os.Bundle
import java.util.Date

internal class UserAccountImpl(
    private val manager: AccountManager,
    private val encryption: EncryptionProvider
) : UserAccount {

    private val account
        get() = manager.getAccountsByType(BuildConfig.AccountType).firstOrNull()

    override val isLoggedIn
        get() = account != null && password != null

    override var email: String?
        get() = account?.name
        set(value) { // todo migrate password when changing email
            account?.also(manager::removeAccountExplicitly)
            if (value != null) {
                val account = Account(value, BuildConfig.AccountType)
                manager.addAccountExplicitly(account, null, Bundle.EMPTY)
            }
        }

    override var password: String?
        get() {
            val encrypted =
                manager.getUserData(account ?: return null, "user-password") ?: return null
            return encryption.decrypt(encrypted).getOrNull()
        }
        set(value) {
            val account = account ?: return
            val encrypted = value?.let(encryption::encrypt)?.getOrNull()
            manager.setUserData(account, "user-password", encrypted)
        }

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

}