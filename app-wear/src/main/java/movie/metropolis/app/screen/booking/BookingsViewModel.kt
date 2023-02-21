package movie.metropolis.app.screen.booking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import movie.metropolis.app.presentation.booking.BookingsFacade
import movie.metropolis.app.presentation.booking.BookingsFacade.Companion.flow
import movie.metropolis.app.util.retainStateIn
import javax.inject.Inject

@HiltViewModel
class BookingsViewModel @Inject constructor(
    facade: BookingsFacade
) : ViewModel() {

    val items = facade.flow
        .retainStateIn(viewModelScope)

}