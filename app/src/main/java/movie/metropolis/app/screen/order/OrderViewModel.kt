package movie.metropolis.app.screen.order

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import movie.metropolis.app.screen.Loadable
import movie.metropolis.app.screen.order.OrderFacade.Companion.requestFlow
import movie.metropolis.app.screen.retainStateIn
import movie.metropolis.app.util.decodeBase64
import javax.inject.Inject

@HiltViewModel
class OrderViewModel(
    facade: OrderFacade
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

}
