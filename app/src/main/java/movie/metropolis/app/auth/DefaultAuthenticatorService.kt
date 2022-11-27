package movie.metropolis.app.auth

import android.accounts.AbstractAccountAuthenticator
import android.app.Service
import android.content.Intent
import android.os.IBinder

open class DefaultAuthenticatorService : Service() {

    protected open var authenticator: AbstractAccountAuthenticator? = null

    override fun onCreate() {
        super.onCreate()
        authenticator = DefaultAccountAuthenticator(applicationContext)
    }

    override fun onDestroy() {
        authenticator = null
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return authenticator?.iBinder
    }

}