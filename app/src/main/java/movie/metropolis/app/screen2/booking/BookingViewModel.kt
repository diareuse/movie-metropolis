package movie.metropolis.app.screen2.booking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import movie.metropolis.app.presentation.booking.BookingFacade
import movie.metropolis.app.util.retainStateIn
import javax.inject.Inject

@HiltViewModel
class BookingViewModel @Inject constructor(
    facade: BookingFacade
) : ViewModel() {

    val tickets = facade.bookings
        .map { it.getOrNull().orEmpty() }
        .retainStateIn(viewModelScope, emptyList())

}