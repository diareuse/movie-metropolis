package movie.metropolis.app.feature.billing

import android.app.Activity
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.ConsumeParams
import com.android.billingclient.api.ProductDetails

class ProductDetailImpl(
    private val client: BillingClientWrapper,
    private val details: ProductDetails
) : ProductDetail {

    override val product: Product
        get() = when (details.productType) {
            BillingClient.ProductType.INAPP -> Product.InApp(details.productId)
            BillingClient.ProductType.SUBS -> Product.Subscription(details.productId)
            else -> throw IllegalArgumentException()
        }
    override val name: String
        get() = details.name
    override val title: String
        get() = details.title
    override val description: String
        get() = details.description
    override val price: String
        get() = when (product) {
            is Product.InApp -> details.oneTimePurchaseOfferDetails?.formattedPrice ?: "Free"
            is Product.Subscription -> details.subscriptionOfferDetails?.first()
                ?.pricingPhases?.pricingPhaseList?.first()
                ?.let { "${it.formattedPrice} / ${it.billingPeriod}" } ?: "Free"
        }

    override suspend fun purchase(activity: Activity) {
        val products = listOf(
            BillingFlowParams.ProductDetailsParams.newBuilder()
                .setProductDetails(details)
                .build()
        )
        val flowParams = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(products)
            .build()
        val (_, purchases) = client.launchBillingFlow(activity, flowParams)
        for (purchase in purchases.orEmpty()) {
            val params = ConsumeParams.newBuilder()
                .setPurchaseToken(purchase.purchaseToken)
                .build()
            client.consumePurchase(params)
        }
    }

}