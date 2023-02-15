package movie.metropolis.app.presentation.order

import movie.metropolis.app.feature.billing.BillingFacade
import movie.metropolis.app.feature.billing.Product
import movie.metropolis.app.model.ProductDetailView
import movie.metropolis.app.model.adapter.ProductDetailViewFromFeature

class OrderCompleteFacadeFromFeature(
    private val facade: BillingFacade
) : OrderCompleteFacade {

    override suspend fun getProducts(): Result<List<ProductDetailView>> = kotlin.runCatching {
        facade.query(Products)
            .sortedBy { detail -> Products.indexOfFirst { it.id == detail.product.id } }
            .map(::ProductDetailViewFromFeature)
    }

    companion object {

        private val Products = buildList {
            this += Product.InApp("consumable.mini")
            this += Product.InApp("consumable.medium")
            this += Product.InApp("consumable.large")
        }

    }

}