package movie.metropolis.app.screen2.purchase

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import movie.metropolis.app.presentation.order.OrderFacade
import movie.metropolis.app.presentation.order.OrderFacade.Companion.isCompletedFlow
import movie.metropolis.app.presentation.order.OrderFacade.Companion.requestFlow
import movie.metropolis.app.screen.Route
import movie.metropolis.app.util.retainStateIn
import javax.inject.Inject

@HiltViewModel
class PurchaseViewModel private constructor(
    private val facade: OrderFacade
) : ViewModel() {

    @Inject
    constructor(
        handle: SavedStateHandle,
        factory: OrderFacade.Factory
    ) : this(
        factory.create(Route.Order.Arguments(handle).url)
    )

    val request = facade.requestFlow
        .map { it.getOrNull() }
        .retainStateIn(viewModelScope, null)

    val isCompleted = facade.isCompletedFlow
        .retainStateIn(viewModelScope, false)

    fun updateUrl(url: String?) {
        facade.setUrl(url.orEmpty())
    }

}