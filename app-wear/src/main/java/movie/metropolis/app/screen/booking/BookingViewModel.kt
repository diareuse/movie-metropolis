package movie.metropolis.app.screen.booking

import androidx.compose.runtime.*
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import movie.metropolis.app.screen.Route
import javax.inject.Inject

@Stable
@HiltViewModel
class BookingViewModel @Inject constructor(
    handle: SavedStateHandle
) : ViewModel() {

    val id = Route.Ticket.Arguments(handle).id

}