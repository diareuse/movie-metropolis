package movie.metropolis.app.feature.global

import android.content.Context
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

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