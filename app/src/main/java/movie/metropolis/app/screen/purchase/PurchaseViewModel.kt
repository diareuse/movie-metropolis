package movie.metropolis.app.screen.purchase

import androidx.compose.runtime.*
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import movie.metropolis.app.presentation.order.OrderFacade
import movie.metropolis.app.screen.Route
import movie.metropolis.app.util.retainStateIn
import javax.inject.Inject

@Stable
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

    val request = flow { emit(facade.getRequest()) }
        .map { it.getOrNull() }
        .retainStateIn(viewModelScope, null)

    val isCompleted = facade.isCompleted
        .retainStateIn(viewModelScope, false)

    fun updateUrl(url: String?) {
        facade.setUrl(url.orEmpty())
    }

}