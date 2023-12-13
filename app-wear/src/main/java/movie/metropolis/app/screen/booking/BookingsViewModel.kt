package movie.metropolis.app.screen.booking

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import movie.metropolis.app.model.TicketView
import movie.metropolis.app.presentation.booking.BookingsFacade
import movie.metropolis.app.presentation.booking.BookingsFacade.Companion.flow
import movie.metropolis.app.presentation.map
import movie.metropolis.app.util.retainStateIn
import javax.inject.Inject

@Stable
@HiltViewModel
class BookingsViewModel @Inject constructor(
    facade: BookingsFacade
) : ViewModel() {

    private val items = facade.flow
        .shareIn(viewModelScope, SharingStarted.Lazily, 1)

    val active = items.map { it.map { it.filterIsInstance<TicketView.Active>() } }
        .retainStateIn(viewModelScope)

    val expired = items.map { it.map { it.filterIsInstance<TicketView.Expired>() } }
        .retainStateIn(viewModelScope)

}