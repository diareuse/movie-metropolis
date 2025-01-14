package movie.metropolis.app.ui.ticket

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import movie.metropolis.app.presentation.booking.BookingFacade
import javax.inject.Inject

@HiltViewModel
class TicketViewModel @Inject constructor(
    private val facade: BookingFacade
) : ViewModel() {

    val state = TicketScreenState()

    init {
        viewModelScope.launch {
            facade.bookings.collect {
                state.tickets.clear()
                state.tickets.addAll(it)
            }
        }
    }

}