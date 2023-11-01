package movie.metropolis.app.screen2.booking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.map
import movie.metropolis.app.presentation.booking.BookingFacade
import movie.metropolis.app.presentation.booking.BookingFacade.Companion.bookingsFlow
import movie.metropolis.app.util.retainStateIn
import javax.inject.Inject

@HiltViewModel
class BookingViewModel @Inject constructor(
    facade: BookingFacade
) : ViewModel() {

    private val refreshFlow = Channel<Unit>(Channel.RENDEZVOUS)
    val tickets = facade.bookingsFlow(refreshFlow.consumeAsFlow().map { { facade.refresh() } })
        .map { it.getOrNull().orEmpty() }
        .retainStateIn(viewModelScope, emptyList())

    fun refresh() {
        refreshFlow.trySend(Unit)
    }

}