package movie.metropolis.app.ui.order

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import movie.metropolis.app.presentation.order.OrderFacade
import movie.metropolis.app.presentation.order.RequestView
import movie.metropolis.app.screen.Route
import movie.metropolis.app.util.retainStateIn
import javax.inject.Inject

@HiltViewModel
class OrderViewModel private constructor(
    private val facade: OrderFacade
) : ViewModel() {

    @Inject
    constructor(
        handle: SavedStateHandle,
        factory: OrderFacade.Factory
    ) : this(
        factory.create(Route.Order.Arguments(handle).url)
    )

    val state = RequestView()

    init {
        viewModelScope.launch {
            state.headers.putAll(facade.getHeaders())
            state.url = facade.url
        }
    }

    val isCompleted = facade.isCompleted
        .retainStateIn(viewModelScope, false)

    fun updateUrl(url: String?) {
        facade.setUrl(url.orEmpty())
    }

}