package movie.metropolis.app.screen.order

import androidx.compose.runtime.Stable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import movie.metropolis.app.presentation.Loadable
import movie.metropolis.app.presentation.order.OrderFacade
import movie.metropolis.app.presentation.order.OrderFacade.Companion.isCompletedFlow
import movie.metropolis.app.presentation.order.OrderFacade.Companion.requestFlow
import movie.metropolis.app.util.decodeBase64
import movie.metropolis.app.util.retainStateIn
import javax.inject.Inject

@Stable
@HiltViewModel
class OrderViewModel(
    private val facade: OrderFacade
) : ViewModel() {

    @Inject
    constructor(
        handle: SavedStateHandle,
        factory: OrderFacade.Factory
    ) : this(
        factory.create(handle.get<String>("url").orEmpty().decodeBase64())
    )

    val request = facade.requestFlow
        .retainStateIn(viewModelScope, Loadable.loading())

    val isCompleted = facade.isCompletedFlow
        .retainStateIn(viewModelScope, false)

    fun updateUrl(url: String?) {
        facade.setUrl(url.orEmpty())
    }

}
