package movie.metropolis.app.feature.billing

interface BillingFacade {

    suspend fun query(products: Iterable<Product>): List<ProductDetail>

}