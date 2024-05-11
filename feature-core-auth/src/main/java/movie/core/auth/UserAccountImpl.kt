package movie.core.auth

import android.accounts.Account
import android.accounts.AccountManager
import android.os.Bundle

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
        set(value) {
            val password = password
            account?.also(manager::removeAccountExplicitly)
            if (value != null) {
                val account = Account(value, BuildConfig.AccountType)
                val bundle = Bundle().apply {
                    putString("user-password", password)
                }
                manager.addAccountExplicitly(account, null, bundle)
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

}