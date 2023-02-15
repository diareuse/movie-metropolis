package movie.metropolis.app.presentation.order

import kotlinx.coroutines.flow.channelFlow
import movie.metropolis.app.model.ProductDetailView
import movie.metropolis.app.presentation.Loadable
import movie.metropolis.app.presentation.asLoadable

interface OrderCompleteFacade {

    suspend fun getProducts(): Result<List<ProductDetailView>>

    companion object {

        val OrderCompleteFacade.productsFlow
            get() = channelFlow {
                send(Loadable.loading())
                send(getProducts().asLoadable())
            }

    }

}