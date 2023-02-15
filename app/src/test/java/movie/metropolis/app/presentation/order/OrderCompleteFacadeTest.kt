package movie.metropolis.app.presentation.order

import android.app.Activity
import kotlinx.coroutines.test.runTest
import movie.metropolis.app.R
import movie.metropolis.app.di.FacadeModule
import movie.metropolis.app.feature.billing.BillingFacade
import movie.metropolis.app.feature.billing.Product
import movie.metropolis.app.feature.billing.ProductDetail
import movie.metropolis.app.util.wheneverBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import kotlin.test.assertEquals

class OrderCompleteFacadeTest {

    private lateinit var facade: OrderCompleteFacade
    private lateinit var billing: BillingFacade

    @Before
    fun prepare() {
        billing = mock()
        facade = FacadeModule().orderComplete(billing)
    }

    @Test
    fun getProducts_returns_correctIcon_mini() = runTest {
        billing_responds_success { copy(Product.InApp("consumable.mini")) }
        val products = facade.getProducts().getOrThrow()
        assertEquals(R.drawable.ic_thumbs_up, products.first().icon)
    }

    @Test
    fun getProducts_returns_correctIcon_medium() = runTest {
        billing_responds_success { copy(Product.InApp("consumable.medium")) }
        val products = facade.getProducts().getOrThrow()
        assertEquals(R.drawable.ic_drink, products.first().icon)
    }

    @Test
    fun getProducts_returns_correctIcon_large() = runTest {
        billing_responds_success { copy(Product.InApp("consumable.large")) }
        val products = facade.getProducts().getOrThrow()
        assertEquals(R.drawable.ic_popcorn, products.first().icon)
    }

    @Test
    fun getProducts_returns_name() = runTest {
        billing_responds_success { copy(name = "expected") }
        val products = facade.getProducts().getOrThrow()
        assertEquals("expected", products.first().name)
    }

    @Test
    fun getProducts_returns_description() = runTest {
        billing_responds_success { copy(description = "expected") }
        val products = facade.getProducts().getOrThrow()
        assertEquals("expected", products.first().description)
    }

    @Test
    fun getProducts_returns_price() = runTest {
        billing_responds_success { copy(price = "expected") }
        val products = facade.getProducts().getOrThrow()
        assertEquals("expected", products.first().price)
    }

    private fun billing_responds_success(modifier: ProductDetail.() -> ProductDetail = { this }) {
        val items = List(1) { mock<ProductDetail>() }.map(modifier)
        wheneverBlocking { billing.query(any()) }.thenReturn(items)
    }

    private fun ProductDetail.copy(
        product: Product = this.product,
        name: String = this.name,
        title: String = this.title,
        description: String = this.description,
        price: String = this.price
    ): ProductDetail {
        return object : ProductDetail {
            override val product: Product = product
            override val name: String = name
            override val title: String = title
            override val description: String = description
            override val price: String = price

            override suspend fun purchase(activity: Activity) =
                this@copy.purchase(activity)
        }
    }

}