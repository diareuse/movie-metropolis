package movie.metropolis.app.screen.order

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.util.decodeBase64String
import movie.metropolis.app.screen.Loadable
import movie.metropolis.app.screen.order.OrderFacade.Companion.requestFlow
import movie.metropolis.app.screen.retainStateIn
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
        factory.create(handle.get<String>("url").orEmpty().decodeBase64String())
    )

    val request = facade.requestFlow
        .retainStateIn(viewModelScope, Loadable.loading())

}
