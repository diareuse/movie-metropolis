package movie.metropolis.app.screen.order.complete

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import movie.metropolis.app.presentation.order.OrderCompleteFacade
import movie.metropolis.app.presentation.order.OrderCompleteFacade.Companion.productsFlow
import movie.metropolis.app.util.retainStateIn
import javax.inject.Inject

@HiltViewModel
class OrderCompleteViewModel @Inject constructor(
    private val facade: OrderCompleteFacade
) : ViewModel() {

    val products = facade.productsFlow
        .retainStateIn(viewModelScope)

}