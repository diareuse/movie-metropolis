package movie.metropolis.app.feature.billing

import com.android.billingclient.api.BillingClient

sealed class Product {

    abstract val id: String

    data class InApp(override val id: String) : Product()
    data class Subscription(override val id: String) : Product()

    internal val type
        get() = when (this) {
            is InApp -> BillingClient.ProductType.INAPP
            is Subscription -> BillingClient.ProductType.SUBS
        }

}