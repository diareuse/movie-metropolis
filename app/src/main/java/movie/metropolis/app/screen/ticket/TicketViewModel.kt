package movie.metropolis.app.screen.ticket

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.map
import movie.metropolis.app.presentation.booking.BookingFacade
import movie.metropolis.app.util.retainStateIn
import javax.inject.Inject

@Stable
@HiltViewModel
class TicketViewModel @Inject constructor(
    facade: BookingFacade
) : ViewModel() {

    private val refreshFlow = Channel<Unit>(Channel.RENDEZVOUS)

    @Suppress("USELESS_CAST")
    val tickets = facade.bookings
        .map { TicketContentState.Success(it.toImmutableList()) as TicketContentState }
        .retainStateIn(viewModelScope, TicketContentState.Loading)

    fun refresh() {
        refreshFlow.trySend(Unit)
    }

}