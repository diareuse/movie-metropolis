package movie.metropolis.app.feature.billing

import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.Purchase

data class BillingFlowResult(
    val result: BillingResult,
    val purchases: MutableList<Purchase>?
)