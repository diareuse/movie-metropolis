package movie.metropolis.app.screen.booking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import movie.metropolis.app.presentation.booking.BookingFacade
import movie.metropolis.app.presentation.booking.BookingFacade.Companion.flow
import movie.metropolis.app.util.retainStateIn
import javax.inject.Inject

@HiltViewModel
class BookingViewModel @Inject constructor(
    facade: BookingFacade
) : ViewModel() {

    val items = facade.flow
        .retainStateIn(viewModelScope)

}