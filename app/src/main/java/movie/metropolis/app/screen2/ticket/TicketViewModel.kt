package movie.metropolis.app.screen2.ticket

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.map
import movie.metropolis.app.presentation.booking.BookingFacade
import movie.metropolis.app.presentation.booking.BookingFacade.Companion.bookingsFlow
import movie.metropolis.app.util.retainStateIn
import javax.inject.Inject

@HiltViewModel
class TicketViewModel @Inject constructor(
    facade: BookingFacade
) : ViewModel() {

    private val refreshFlow = Channel<Unit>(Channel.RENDEZVOUS)

    @Suppress("USELESS_CAST")
    val tickets = facade.bookingsFlow(refreshFlow.consumeAsFlow().map { { facade.refresh() } })
        .map { TicketContentState.Success(it.toImmutableList()) as TicketContentState }
        .catch { emit(TicketContentState.Failure(it)) }
        .retainStateIn(viewModelScope, TicketContentState.Loading)

    fun refresh() {
        refreshFlow.trySend(Unit)
    }

}