package movie.metropolis.app.screen2.purchase

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.map
import movie.metropolis.app.presentation.order.OrderCompleteFacade
import movie.metropolis.app.presentation.order.OrderCompleteFacade.Companion.productsFlow
import movie.metropolis.app.util.retainStateIn
import javax.inject.Inject

@HiltViewModel
class PurchaseCompleteViewModel @Inject constructor(
    facade: OrderCompleteFacade
) : ViewModel() {

    val products = facade.productsFlow
        .map { it.getOrNull().orEmpty().toImmutableList() }
        .retainStateIn(viewModelScope, persistentListOf())

}