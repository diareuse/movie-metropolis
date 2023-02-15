package movie.metropolis.app.feature.billing

import android.content.Context
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.QueryProductDetailsParams

class BillingFacadeImpl(context: Context) : BillingFacade {

    private val client: BillingClientWrapper

    init {
        val listener = BillingClientWrapper.Listener()
        val client = BillingClient.newBuilder(context)
            .setListener(listener)
            .enablePendingPurchases()
            .build()
        this.client = BillingClientWrapper(client, listener)
    }

    override suspend fun query(products: Iterable<Product>): List<ProductDetail> {
        val list = products.map {
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId(it.id)
                .setProductType(it.type)
                .build()
        }
        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(list)
            .build()
        val (result, details) = client.queryProductDetails(params)
        if (result.responseCode != BillingClient.BillingResponseCode.OK) return emptyList()
        return details.orEmpty()
            .map { ProductDetailImpl(client, it) }
    }

}