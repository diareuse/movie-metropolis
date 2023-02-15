package movie.metropolis.app.feature.billing

import android.app.Activity
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ConsumeParams
import com.android.billingclient.api.ConsumeResult
import com.android.billingclient.api.ProductDetailsResult
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.consumePurchase
import com.android.billingclient.api.queryProductDetails
import movie.metropolis.app.presentation.Listenable
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class BillingClientWrapper(
    private val client: BillingClient,
    private val listener: Listener
) {

    suspend fun queryProductDetails(params: QueryProductDetailsParams): ProductDetailsResult {
        return client.awaitConnection().queryProductDetails(params)
    }

    suspend fun consumePurchase(params: ConsumeParams): ConsumeResult {
        return client.awaitConnection().consumePurchase(params)
    }

    suspend fun launchBillingFlow(
        activity: Activity,
        params: BillingFlowParams
    ): BillingFlowResult {
        val client = client.awaitConnection()
        return suspendCoroutine { continuation ->
            listener.setListener { result, purchases ->
                continuation.resume(BillingFlowResult(result, purchases))
            }
            client.launchBillingFlow(activity, params)
        }
    }

    class Listener : PurchasesUpdatedListener {

        private val listenable = Listenable<PurchasesUpdatedListener>()

        override fun onPurchasesUpdated(result: BillingResult, purchases: MutableList<Purchase>?) {
            listenable.notify { onPurchasesUpdated(result, purchases) }
        }

        fun setListener(listener: PurchasesUpdatedListener) {
            addListener(object : PurchasesUpdatedListener {
                override fun onPurchasesUpdated(p0: BillingResult, p1: MutableList<Purchase>?) {
                    listener.onPurchasesUpdated(p0, p1)
                    removeListener(this)
                }
            })
        }

        private fun addListener(listener: PurchasesUpdatedListener): PurchasesUpdatedListener {
            listenable += listener
            return listener
        }

        private fun removeListener(listener: PurchasesUpdatedListener) {
            listenable -= listener
        }

    }

}