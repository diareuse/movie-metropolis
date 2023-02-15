package movie.metropolis.app.feature.billing

import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClient.BillingResponseCode
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingResult
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

@Throws(BillingException::class)
suspend fun BillingClient.awaitConnection() = suspendCoroutine {
    if (isReady) return@suspendCoroutine it.resume(this@awaitConnection)
    startConnection(object : BillingClientStateListener {
        override fun onBillingServiceDisconnected() {}
        override fun onBillingSetupFinished(result: BillingResult) {
            when (result.responseCode) {
                BillingResponseCode.OK -> it.resume(this@awaitConnection)
                else -> it.resumeWithException(BillingException(result))
            }
        }
    })
}