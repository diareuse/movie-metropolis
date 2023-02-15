package movie.metropolis.app.feature.billing

import com.android.billingclient.api.BillingResult

class BillingException(val result: BillingResult) : IllegalStateException()