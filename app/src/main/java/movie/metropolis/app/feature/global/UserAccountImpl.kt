package movie.metropolis.app.feature.global

import android.accounts.Account
import android.accounts.AccountManager
import android.os.Build
import android.os.Bundle
import java.util.Date

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

    override fun delete() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            manager.removeAccountExplicitly(account ?: return)
        }
    }

    companion object {

        private const val Type = "movie.metropolis"

    }

}